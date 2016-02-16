package net.jeeshop.web.action.front;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.jeeshop.core.FrontContainer;
import net.jeeshop.core.KeyValueHelper;
import net.jeeshop.core.Services;
import net.jeeshop.core.dao.page.PagerModel;
import net.jeeshop.core.front.SystemManager;
import net.jeeshop.services.front.account.bean.Account;
import net.jeeshop.services.front.catalog.bean.Catalog;
import net.jeeshop.services.front.comment.CommentService;
import net.jeeshop.services.front.comment.bean.Comment;
import net.jeeshop.services.front.emailNotifyProduct.EmailNotifyProductService;
import net.jeeshop.services.front.emailNotifyProduct.bean.EmailNotifyProduct;
import net.jeeshop.services.front.product.ProductService;
import net.jeeshop.services.front.product.bean.Product;
import net.jeeshop.services.manage.activity.bean.Activity;
import net.jeeshop.services.manage.spec.SpecService;
import net.jeeshop.services.manage.spec.bean.Spec;
import net.jeeshop.web.action.front.orders.CartInfo;
import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * 前端首页
 */
@Controller
@RequestMapping("/")
public class IndexAction extends FrontBaseController<CartInfo> {	
	@Autowired
	private ProductService productService;//商品服务
	@Autowired
	private SpecService specService;
	@Autowired
	private EmailNotifyProductService emailNotifyProductService;//商品到货通知
	@Autowired
	private CommentService commentService;//评论服务
	
	
    @RequestMapping({"/"})
    public String index(ModelMap model) {
    	try {
			initCart(model);//初始化购物车
			initProduct(model,"10253");//初始化商品
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	//return "pages/index";
    	return "pages/product";
    	//return "index";
    }
    
    @RequestMapping({"/index"})
    public String indexs() {
    	return "index";
    }
	@Override
	public Services<CartInfo> getService() {
		return null;
	}
    
	
	//初始化购物车信息
    private void initCart(ModelMap model){
    	List<Product> productList = new ArrayList<Product>();
		CartInfo cartInfo = getMyCart();
		if(cartInfo!=null){
			productList = cartInfo.getProductList();
		} else {
			cartInfo = new CartInfo();
		}
		model.addAttribute("cartInfo", cartInfo);
		model.addAttribute("productList", productList);
    }
    
    /**
	 * ajax检查密码是否正确
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("getProductList")
	@ResponseBody
	public String getProductList() throws IOException{
		List<Product> productList = new ArrayList<Product>();
		CartInfo cartInfo = getMyCart();
		productList = cartInfo.getProductList();
		JSONArray.fromObject(productList).toString();
			
		if(StringUtils.isBlank(phonenum)){
			return ("{\"msg\":\"phonenumIsNull\"}");
		}else{
			
			Account acc = new Account();
			acc.setAccount(phonenum);
			if(accountService.selectCount(acc)==0){
				return ("{\"msg\":\"notExist\"}");
			}else{
				return ("{\"msg\":\"exist\"}");
			}
		}
	}
    
    
  //初始化购物车信息
    private void initProduct(ModelMap model,String id) throws Exception{

		Product e = productService.selectById(id);
		Catalog item = productService.checkProduct(e);//检查商品信息
		
		productService.saveHistoryProductToSession(e);
		
		//加载商品规格，以JSON字符串的形式隐藏在页面上，然后页面将其转换成对象集合，通过脚本控制规格的颜色和尺寸的双向关系。
		Spec spec = new Spec();
		spec.setProductID(e.getId());
		List<Spec> specList = specService.selectList(spec);
		if(specList!=null && specList.size()>0){
			e.setSpecJsonString(JSON.toJSONString(specList));
			logger.error("e.setSpecJsonString = " + e.getSpecJsonString());
			
//			Set<String> specColor = new HashSet<String>(); 
//			Set<String> specSize = new HashSet<String>();
			
			if(e.getSpecColor()==null){
				e.setSpecColor(new HashSet<String>());
			}
			if(e.getSpecSize()==null){
				e.setSpecSize(new HashSet<String>());
			}
			
			//分离商品的尺寸和颜色
			for(int i=0;i<specList.size();i++){
				Spec specItem = specList.get(i);
				if(StringUtils.isNotBlank(specItem.getSpecColor())){
					e.getSpecColor().add(specItem.getSpecColor());
				}
				if(StringUtils.isNotBlank(specItem.getSpecSize())){
					e.getSpecSize().add(specItem.getSpecSize());
				}
			}
		}
		
		//取key-value
		String unitValue = KeyValueHelper.get("product_unit_"+e.getUnit());
		e.setUnit(unitValue);
		
		if(item.getPid().equals("0")){//主类别
			e.setMainCatalogName(item.getName());
		}else{//子类别
			e.setChildrenCatalogName(item.getName());
			e.setChildrenCatalogCode(item.getCode());
			item = systemManager.getCatalogsMap().get(item.getPid());
//			getSession().setAttribute("selectMenu", item.getId());//商品属于的大类别就是菜单
			e.setMainCatalogName(item.getName());
			
			model.addAttribute("childrenCatalogCode", item.getCode());
			model.addAttribute("hotProductList", SystemManager.getInstance().getProductsByCatalogCode(item.getCode()));
		}
		
		//如果商品已上架并且商品的库存数小于等于0，则更新商品为已下架
		if(e.getStatus()==Product.Product_status_y){
			if(e.getStock()<=0){
				e.product_sorry_str = "抱歉，商品已售卖完了。";
				
				//更新商品为已下架
//				Product p = new Product();
//				p.setId(e.getId());
//				p.setStatus(Product.Product_status_n);
//				productService.update(p);
			}
		}
		
		StringBuilder imagesBuff = new StringBuilder(e.getPicture() + FrontContainer.product_images_spider);
		//组装商品详情页的图片地址
		if(StringUtils.isNotBlank(e.getImages())){
			imagesBuff.append(e.getImages());
		}
		productService.productImagesBiz(imagesBuff.toString(), e);
		
		//加载商品参数
		e.setParameterList(productService.selectParameterList(e.getId()));
		
		//更新商品浏览次数
		Product p = new Product();
		p.setId(e.getId());
//		p.setHit(e.getHit()+1);//浏览次数++
		productService.updateHit(p);

		/*
		 * 加载和这个商品有关联的畅销商品和特价商品，显示到商品明细页面的左侧。
		 * 有关联的商品的选择方法是：加载该商品所在的子目录下的特定商品。考虑到性能问题，
		 * 这个必须借助缓存，事先我们将一些子目录下的畅销商品、特价商品 的前10来个加载到内存，然后用户访问这个页面的时候直接取内存即可。
		 */
//		e.setLeftProducts(loadProducts(1));
		
		
		String url = "/jsp/product/"+e.getId()+".jsp";
		logger.error("url = " + url);
		model.addAttribute("productHTMLUrl", url);
		
		/**
		 * 是否需要显示到货通知的按钮
		 */
		if(e.getStock()<=0){
			Account acc = getLoginAccount();
			if(acc!=null){
				//如果用户之前没有填写过到货通知的申请，则可以提示用户填写。
				EmailNotifyProduct ep = new EmailNotifyProduct();
				ep.setAccount(acc.getAccount());
				ep.setProductID(e.getId());
				if(emailNotifyProductService.selectCount(ep)<=0){
					e.setShowEmailNotifyProductInput(true);
				}
			}
		}
		
		
		
		/*
		 * 检查，如果此商品是活动商品，则加载相应的活动信息。
		 */
		logger.error("e.getActivityID() = "+e.getActivityID());
		if(StringUtils.isNotBlank(e.getActivityID())){
			logger.error(">>>计算或拷贝此商品关联的活动的信息到此商品对象上。展示页面用==");
			Activity activity = systemManager.getActivityMap().get(e.getActivityID());
			
			/**
			 * 计算或拷贝此商品关联的活动的信息到此商品对象上。展示页面用
			 */
			e.setFinalPrice(String.valueOf(e.caclFinalPrice()));
			e.setExpire(activity.isExpire());
			e.setActivityEndDateTime(activity.getActivityEndDateTime());
			e.setActivityType(activity.getActivityType());
			e.setDiscountType(activity.getDiscountType());
			e.setDiscountFormat(activity.getDiscountFormat());
			e.setActivityEndDateTime(activity.getEndDate());
			e.setMaxSellCount(activity.getMaxSellCount());
			e.setAccountRange(activity.getAccountRange());
			e.setExchangeScore(activity.getExchangeScore());
			e.setTuanPrice(activity.getTuanPrice());
			
			logger.error("finalPrice = " + e.getFinalPrice()+",expire = " + e.isExpire()+",activityEndDateTime="+e.getActivityEndDateTime()+",score="+e.getScore());
		
			/*
			 * 如果商品是活动商品，则查看商品明细页的时候自动选择导航菜单li
			 */
			String topMenu = "";
			if(activity.getActivityType().equals(Activity.activity_activityType_c)){
				topMenu = "activity";
			}else if(activity.getActivityType().equals(Activity.activity_activityType_j)){
				topMenu = "score";
			}else if(activity.getActivityType().equals(Activity.activity_activityType_t)){
				topMenu = "tuan";
			}
			model.addAttribute("topMenu", topMenu);
		}
		//加载指定商品的评论
		Comment comment = new Comment();
		comment.setProductID(e.getId());
		PagerModel commentPager = selectPageList(commentService, comment);
		model.addAttribute("pager", commentPager);
		model.addAttribute("e", e);
		model.addAttribute("saleProducts", systemManager.getSaleProducts());
		model.addAttribute("commentTypeCode", systemManager.getCommentTypeCode());
	
    }
    
}
