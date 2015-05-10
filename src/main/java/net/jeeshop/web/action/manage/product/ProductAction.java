package net.jeeshop.web.action.manage.product;import com.alibaba.fastjson.JSON;import net.jeeshop.core.ManageContainer;import net.jeeshop.core.front.SystemManager;import net.jeeshop.core.oscache.ManageCache;import net.jeeshop.core.system.bean.User;import net.jeeshop.services.front.product.bean.ProductStockInfo;import net.jeeshop.services.manage.attribute.AttributeService;import net.jeeshop.services.manage.attribute.bean.Attribute;import net.jeeshop.services.manage.attribute_link.Attribute_linkService;import net.jeeshop.services.manage.attribute_link.bean.Attribute_link;import net.jeeshop.services.manage.gift.GiftService;import net.jeeshop.services.manage.gift.bean.Gift;import net.jeeshop.services.manage.product.ProductService;import net.jeeshop.services.manage.product.bean.Product;import net.jeeshop.services.manage.spec.SpecService;import net.jeeshop.services.manage.spec.bean.Spec;import net.jeeshop.web.action.BaseController;import net.jeeshop.web.util.LoginUserHolder;import net.jeeshop.web.util.RequestHolder;import org.apache.commons.lang.StringUtils;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.ui.ModelMap;import org.springframework.web.bind.annotation.ModelAttribute;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestMethod;import org.springframework.web.bind.annotation.ResponseBody;import org.springframework.web.servlet.mvc.support.RedirectAttributes;import javax.servlet.http.HttpServletRequest;import java.io.File;import java.io.IOException;import java.util.*;/** * 商品信息管理 *  * @author jqsl2012@163.com * @author dylan *  */@Controller@RequestMapping("/manage/product/")public class ProductAction extends BaseController<Product> {	private static final Logger logger = LoggerFactory.getLogger(ProductAction.class);	private static final long serialVersionUID = 1L;	@Autowired	private ProductService productService;	@Autowired	private AttributeService attributeService;	@Autowired	private Attribute_linkService attribute_linkService;	@Autowired	private ManageCache manageCache;	@Autowired	private SpecService specService;	@Autowired	private GiftService giftService;	private static final String page_toList = "/manage/product/productList";	private static final String page_toEdit = "/manage/product/productEdit";	private static final String page_toAdd = "/manage/product/productEdit";	private ProductAction() {		super.page_toList = page_toList;		super.page_toAdd = page_toAdd;		super.page_toEdit = page_toEdit;	}	public GiftService getGiftService() {		return giftService;	}	public void setGiftService(GiftService giftService) {		this.giftService = giftService;	}	public SpecService getSpecService() {		return specService;	}	public void setSpecService(SpecService specService) {		this.specService = specService;	}	public AttributeService getAttributeService() {		return attributeService;	}	public ManageCache getManageCache() {		return manageCache;	}	public void setManageCache(ManageCache manageCache) {		this.manageCache = manageCache;	}	public void setAttributeService(AttributeService attributeService) {		this.attributeService = attributeService;	}	public Attribute_linkService getAttribute_linkService() {		return attribute_linkService;	}	public void setAttribute_linkService(Attribute_linkService attribute_linkService) {		this.attribute_linkService = attribute_linkService;	}	public ProductService getService() {		return productService;	}	public void setProductService(ProductService productService) {		this.productService = productService;	}	/**	 * 添加商品	 */	@Override	public String toAdd(@ModelAttribute("e") Product e, ModelMap model) throws Exception {        model.addAttribute("catalogs", systemManager.getCatalogs());		String chanageCatalog = RequestHolder.getRequest().getParameter("chanageCatalog");		if(StringUtils.isNotBlank(chanageCatalog)){			if(Boolean.valueOf(chanageCatalog)){				String catalog = RequestHolder.getRequest().getParameter("catalog");//新目录				logger.error("添加商品-修改目录 。catalog = " + catalog + ",chanageCatalog = " + chanageCatalog);//				e.clear();				e.setCatalogID(catalog);								//加载指定类别下商品属性和参数				changeCatalog(e, true);//				return page_toAdd;			}else{				throw new NullPointerException("请求非法！");			}		}				List<Gift> giftList = loadGiftList();		model.addAttribute("giftList", giftList);				return super.toAdd(e, model);	}		/**	 * 加载商品赠品列表	 */	private List<Gift> loadGiftList(){		Gift gift = new Gift();		gift.setStatus(Gift.gift_status_up);		List<Gift> giftList = giftService.selectList(gift);		return giftList;	}		//列表页面点击 编辑商品	@Override	public String toEdit(@ModelAttribute("e")Product e, ModelMap model) throws Exception {//		getSession().setAttribute("insertOrUpdateMsg", "");		return toEdit0(e, model);	}	/**	 * 修改商品的类别，会联动清除商品已有的属性和参数	 * @return	 * @throws Exception	 */	@RequestMapping(value = "updateProductCatalog", method = RequestMethod.POST)	public String updateProductCatalog(Product e, ModelMap model) throws Exception {//		getSession().setAttribute("insertOrUpdateMsg", "");		return toEdit0(e, model);	}		/**	 * 添加或编辑商品后程序回转编辑	 * @return	 * @throws Exception	 */	@RequestMapping(value = "toEdit2")	public String toEdit2(Product e, ModelMap model) throws Exception {		return toEdit0(e, model);	}	/**	 * 根据商品ID，加载商品全部信息	 */	private String toEdit0(Product e, ModelMap model) throws Exception {        RequestHolder.getRequest().setAttribute("catalogs", systemManager.getCatalogs());		if(StringUtils.isBlank(e.getId())){			throw new NullPointerException("商品ID不能为空！");		}				e = getService().selectById(e.getId());		if(e==null || StringUtils.isBlank(e.getId())){			throw new NullPointerException("根据商品ID查询不到指定的商品！");		}		model.addAttribute("e", e);				//加载商品图片列表		if(StringUtils.isNotBlank(e.getImages())){			if(e.getImagesList()==null){				e.setImagesList(new LinkedList<String>());			}else{				e.getImagesList().clear();			}			String[] _images = e.getImages().split(ManageContainer.product_images_spider);			for(int i=0;i<_images.length;i++){				if(StringUtils.isNotBlank(_images[i])){					e.getImagesList().add(_images[i]);				}			}		}else{			if(e.getImagesList()==null){				e.setImagesList(Collections.EMPTY_LIST);			}else{				e.getImagesList().clear();			}		}		//如果未切换商品目录，则加载商品目录		if(!changeCatalog(e, false)){			if(StringUtils.isNotBlank(e.getCatalogID())){				int catalogID = Integer.valueOf(e.getCatalogID());				loadAttribute(e, catalogID);				loadParameter(e, catalogID);				loadSpec(e);			}		}		List<Gift> giftList = loadGiftList();		model.addAttribute("giftList", giftList);		return page_toEdit;	}		/**	 * 加载商品规格	 */	private void loadSpec(Product p){		if(StringUtils.isBlank(p.getId())){			logger.error("loadSpec id = " + p.getId());			return;		}				Spec specInfo = new Spec();		specInfo.setProductID(p.getId());		p.setSpecList(specService.selectList(specInfo));				if(p.getSpecList()!=null){			logger.error("loadSpec = p.getSpecList() = " + p.getSpecList().size());		}else{			logger.error("loadSpec = p.getSpecList() is null");		}				if(p.getSpecList()!=null && p.getSpecList().size() > 0){						//如果有规格，则添加3个到集合的最后，以方便添加数据			for(int i=0;i<3;i++){				p.getSpecList().add(new Spec());			}					}else{						//如果没有规格，则默认添加10个空的，以方便添加数据			if(p.getSpecList()==null){				p.setSpecList(new ArrayList<Spec>(10));			}			for(int i=0;i<10;i++){				p.getSpecList().add(new Spec());			}		}	}		/**	 * 如果添加或编辑商品的时候切换了商品目录，则该商品的属性和参数得重新加载。	 * @return true：重新加载商品的属性和参数。	 */	private boolean changeCatalog(Product e, boolean toAdd){		String chanageCatalog = RequestHolder.getRequest().getParameter("chanageCatalog");		if(toAdd){			chanageCatalog = "true";		}				if(StringUtils.isNotBlank(chanageCatalog)){			if(Boolean.valueOf(chanageCatalog)){//				getSession().setAttribute("insertOrUpdateMsg", "改变商品目录，已重新加载了商品的属性和参数。");				int catalog = Integer.valueOf(RequestHolder.getRequest().getParameter("catalog"));				logger.error("catalogID====="+catalog);				//删除该商品之前的目录对应的属性和参数				if(StringUtils.isNotBlank(e.getId())){					Attribute_link attrLink = new Attribute_link();					attrLink.setProductID(Integer.valueOf(e.getId()));					attribute_linkService.deleteByCondition(attrLink);				}								e.setCatalogID(String.valueOf(catalog));				//切换商品目录，则自动切换商品属性和参数				loadAttribute(e, catalog);				loadParameter(e, catalog);				return true;			}		}		return false;	}	/**	 * 根据商品分类加载商品属性列表	 * @catalogID 商品类别ID	 */	private void loadParameter(Product e, int catalogID) {		Attribute attr = new Attribute();		attr.setCatalogID(catalogID);		attr.setPid(-1);		attr = this.attributeService.selectOne(attr);//加载参数主属性，一个参数下包含多个子参数		if(attr!=null){			//加载每个属性下的子属性列表			int id = Integer.valueOf(attr.getId());			attr.clear();			attr.setPid(id);//			attr.setPid(0);			attr.setCatalogID(0);			//@@@			e.setParameterList(this.attributeService.selectList(attr));		}				//如果商品ID不存在，则不加载商品选中的参数列表		if(StringUtils.isBlank(e.getId())){			return;		}				//加载商品参数		if(e.getParameterList()!=null && e.getParameterList().size()>0){			Attribute_link attrLink = new Attribute_link();			attrLink.setProductID(Integer.valueOf(e.getId()));			//查询参数列表			List<Attribute_link> attrLinkList = attribute_linkService.selectList(attrLink);			if(attrLinkList!=null && attrLinkList.size()>0){								for(int i=0;i<e.getParameterList().size();i++){//循环主属性					Attribute itemInfo = e.getParameterList().get(i);					int _attrID = Integer.valueOf(itemInfo.getId());					for(int k=0;k<attrLinkList.size();k++){//循环用户选择的属性						Attribute_link al = attrLinkList.get(k);						if(al.getAttrID()==_attrID){							itemInfo.setParameterValue(al.getValue());							break;						}					}				}							}		}	}	/**	 * 根据商品分类加载商品属性列表	 * @catalogID 商品类别ID	 */	private void loadAttribute(Product e, int catalogID) {		Attribute attr = new Attribute();		attr.setCatalogID(catalogID);		List<Attribute> attrList = this.attributeService.selectList(attr);		//加载每个属性下的子属性列表		if(attrList!=null && attrList.size()>0){			attr.setCatalogID(0);			attr.setPid(0);//属性的			for(int i=0;i<attrList.size();i++){				Attribute item = attrList.get(i);				attr.setPid(Integer.valueOf(item.getId()));				//###				item.setAttrList(this.attributeService.selectList(attr));			}		}		e.setAttrList(attrList);				//如果商品ID不存在，则不加载商品选中的属性列表		if(StringUtils.isBlank(e.getId())){			return;		}				//加载商品所选中的属性列表		Attribute_link attrLink = new Attribute_link();		attrLink.setProductID(Integer.valueOf(e.getId()));		List<Attribute_link> attrLinkList = attribute_linkService.selectList(attrLink);		if(attrLinkList!=null && attrLinkList.size()>0){			if(e.getAttrList()!=null && e.getAttrList().size()>0){//				for(int i=0;i<attrLinkList.size();i++){//					Attribute_link al = attrLinkList.get(i);//					loop:for(int j=0;j<e.getAttrList().size();j++){//						List<Attribute> attrItemList = e.getAttrList().get(j).getAttrList();//						for(int k=0;k<attrItemList.size();k++){//							Attribute attrInfo = attrItemList.get(k);//							int _selected = Integer.valueOf(attrInfo.getId());//							//选中用户设置的属性//							if(al.getAttrID()==_selected){//								attrInfo.setSelectedID(_selected);//								break loop;//							}//						}//					}//				}												for(int i=0;i<e.getAttrList().size();i++){//循环主属性					Attribute mainAttr = e.getAttrList().get(i);					List<Attribute> itemList = mainAttr.getAttrList();					loop:for(int j=0;j<itemList.size();j++){//循环子属性列表						Attribute itemInfo = itemList.get(j);						int _attrID = Integer.valueOf(itemInfo.getId());						for(int k=0;k<attrLinkList.size();k++){//循环用户选择的属性							Attribute_link al = attrLinkList.get(k);							if(al.getAttrID()==_attrID){								mainAttr.setSelectedID(_attrID);								break loop;							}						}					}				}							}		}	}	//分页查询商品	@Override	public String selectList(HttpServletRequest request,@ModelAttribute("e") Product e) throws Exception {		try {            RequestHolder.getRequest().setAttribute("catalogs", systemManager.getCatalogs());			e.setQueryCatalogIDs(SystemManager.getInstance().getCatalogsById(e.getCatalogID()));			super.selectList(request, e);			return page_toList;		} catch (Exception ex) {			ex.printStackTrace();			throw ex;		}	}		@Override	protected void setParamWhenInitQuery(Product e) {		super.setParamWhenInitQuery(e);		String selectOutOfStockProduct = RequestHolder.getRequest().getParameter("selectOutOfStockProduct");		if(StringUtils.isNotBlank(selectOutOfStockProduct)){			//后台--首页 需要查询缺货商品			e.setSelectOutOfStockProduct(Boolean.valueOf(selectOutOfStockProduct));		}	}	/**	 * ajax查询指定商品的图片集合	 * @return	 */	@Deprecated	@RequestMapping("ajaxLoadImgList")	@ResponseBody	public String ajaxLoadImgList(){		String id = RequestHolder.getRequest().getParameter("id");		String path = RequestHolder.getRequest().getSession().getServletContext().getRealPath("/");		System.out.println("path=" + path);//		path = path.substring(0, path.indexOf("WEB-INF"));//		System.out.println("path=" + path);		path = path+"/upload/"+id+"/";		System.out.println("path=" + path);				File dir = new File(path);		File[] fiels = dir.listFiles();		List<String> fileList = new LinkedList<String>();		if(fiels!=null && fiels.length>0){			String www_address = systemManager.getProperty("www_address");            for (int i=0;i<fiels.length;i++){				fileList.add(www_address + "/upload/"+id+"/"+fiels[i].getName());			}		}		String json = JSON.toJSONString(fileList);		System.out.println(json);		try {			return (json);		} catch (Exception ex) {			ex.printStackTrace();		}		return null;	}		/**	 *  添加产品	 */	@Override	public String insert(HttpServletRequest request, Product e, RedirectAttributes flushAttrs) throws Exception {		logger.error(">>>insert product...");		//设置产品的图片路径		e.setImages(getImagesPath(e, null));		e.setStatus(1);		e.setCreateAccount(getAccount());		//		if(e.getPicture().indexOf(""))		int productID = getService().insert(e);		e.clear();		e.setId(String.valueOf(productID));		insertOrUpdateCommon(e);//		clearCache();//		getSession().setAttribute("insertOrUpdateMsg", "添加产品成功！");//		getResponse().sendRedirect(getEditUrl(String.valueOf(productID)));		return "redirect:toEdit2?id="+ e.getId();	}		//获取后台管理人员的账号	private String getAccount(){		User user = LoginUserHolder.getLoginUser();		if(user==null){			throw new NullPointerException("登陆超时！");		}		return user.getUsername();	}		/**	 * 更新产品	 */	@Override	public String update(HttpServletRequest request, Product e, RedirectAttributes flushAttrs) throws Exception {		logger.error(">>>update product..."+e.getCatalogID());				String id = e.getId();		Product ee = productService.selectById(id);				boolean loadReport = false;		//如果库存原来是0,现在变成大于0的了,那么需要重新加载商品库存数据		if (ee.getStock() <= 0 && e.getStock() > 0) {			loadReport = true;		}						//设置产品的图片路径		e.setImages(getImagesPath(e, ee.getImages()));		e.setUpdateAccount(getAccount());		getService().update(e);		e.clear();		e.setId(id);//		getE().setStatus(1);//		getE().setName(null);				insertOrUpdateCommon(e);				if(loadReport){			manageCache.loadOrdersReport();		}//		clearCache();//		updateMsg = "更新产品成功！";//		getSession().setAttribute("insertOrUpdateMsg", "更新产品成功！");//		getResponse().sendRedirect(getEditUrl(id));		return "redirect:toEdit2?id="+ e.getId();	}		/**	 * 添加或更新商品的公共功能	 * @throws IOException	 */	private void insertOrUpdateCommon(Product e) throws IOException {		logger.error("=insertOrUpdateCommon=");				/**		 * 同步内存商品库存数据		 */        Map<String, ProductStockInfo> productStockMap = systemManager.getProductStockMap();        ProductStockInfo momeryProduct = productStockMap.get(e.getId());		List<String> productIDs = new LinkedList<String>();		productIDs.add(e.getId());		Product proObject = productService.selectStockByIDs(productIDs).get(0);				if(momeryProduct==null){			ProductStockInfo p = new ProductStockInfo();			p.setId(proObject.getId());			p.setStock(proObject.getStock());			p.setScore(proObject.getScore());			productStockMap.put(proObject.getId(), p);            //update stock map            systemManager.setProductStockMap(productStockMap);		}else{			momeryProduct.setStock(proObject.getStock());			momeryProduct.setScore(proObject.getScore());		}				//上传图片//		uploadImages();				//删除产品旧的属性列表		Attribute_link oldAttr = new Attribute_link();		oldAttr.setProductID(Integer.valueOf(e.getId()));		attribute_linkService.deleteByCondition(oldAttr);				//保存商品属性		e.setAttrSelectIds(RequestHolder.getRequest().getParameterValues("attrSelectIds"));		logger.error("attrSelectIds="+e.getAttrSelectIds());		if(e.getAttrSelectIds()!=null && e.getAttrSelectIds().length>0){			for(int i=0;i<e.getAttrSelectIds().length;i++){				String attrID = e.getAttrSelectIds()[i];				if(StringUtils.isBlank(attrID)){					continue;				}				//插入数据到属性中间表				Attribute_link attrLink = new Attribute_link();				attrLink.setAttrID(Integer.valueOf(attrID));				attrLink.setProductID(Integer.valueOf(e.getId()));				attribute_linkService.insert(attrLink);			}		}				//保存商品参数		e.setParameterIds(RequestHolder.getRequest().getParameterValues("id"));		e.setParameterNames(RequestHolder.getRequest().getParameterValues("parameterValue"));		if(e.getParameterNames()!=null && e.getParameterNames().length>0){			for(int i=0;i<e.getParameterNames().length;i++){				String pName = e.getParameterNames()[i];				if(StringUtils.isBlank(pName)){					continue;				}				//插入数据到属性中间表				Attribute_link attrLink = new Attribute_link();				attrLink.setAttrID(Integer.valueOf(e.getParameterIds()[i]));				attrLink.setValue(pName);				attrLink.setProductID(Integer.valueOf(e.getId()));				attribute_linkService.insert(attrLink);			}		}	}		/**	 * 例如：http://127.0.0.1:8082/myshop/upload/1.jpg;http://127.0.0.1:8082/myshop/upload/2.jpg;	 * 获取产品图片路径，注意，这个应该都是相对路径，因为图片有可能会放到专门的图片服务器上。	 * @return	 */	private String getImagesPath(Product e, String appendImgs){		logger.error("e.images = "+e.getImages());//		if(StringUtils.isBlank(e.getImages())){//			return null;//		}		Set<String> imagesSet = new HashSet<String>();				//添加库里面查询出的图片		if(StringUtils.isNotBlank(appendImgs)){			String[] images2 = appendImgs.split(ManageContainer.product_images_spider);			for(int i=0;i<images2.length;i++){				if(StringUtils.isNotBlank(images2[i])){					imagesSet.add(images2[i].trim());				}			}		}				//添加页面上传的图片		String[] images = e.getImages().split(ManageContainer.product_images_spider);		for(int i=0;i<images.length;i++){			if(StringUtils.isNotBlank(images[i])){				imagesSet.add(images[i].trim());			}		}				//图片转为逗号分割形式		StringBuilder buff = new StringBuilder();		for(Iterator<String> it = imagesSet.iterator();it.hasNext();){			buff.append(it.next()+",");		}		String rr = buff.toString();		if(rr.length()>0 && rr.endsWith(ManageContainer.product_images_spider)){			rr = rr.substring(0, rr.length()-1);		}		return rr;	}		/**	 * 上架指定商品	 * @return	 * @throws Exception 	 */	@RequestMapping(value = "updateUpProduct", method = RequestMethod.POST)	public String updateUpProduct(Product e) throws Exception{		if(StringUtils.isBlank(e.getId())){			throw new NullPointerException();		}				User user = LoginUserHolder.getLoginUser();		productService.updateProductStatus(new String[]{e.getId()},Product.Product_status_y,user.getUsername());//		getSession().setAttribute("insertOrUpdateMsg", "上架成功！");//		getResponse().sendRedirect(getEditUrl(e.getId()));		return "redirect:toEdit2?id="+e.getId();	}		/**	 * 下架指定商品	 * @return	 * @throws Exception 	 */	@RequestMapping(value = "updateDownProduct", method = RequestMethod.POST)	public String updateDownProduct(Product e) throws Exception{		if(StringUtils.isBlank(e.getId())){			throw new NullPointerException();		}		User user = LoginUserHolder.getLoginUser();		productService.updateProductStatus(new String[]{e.getId()},Product.Product_status_n,user.getUsername());//		getSession().setAttribute("insertOrUpdateMsg", "下架成功！");//		getResponse().sendRedirect(getEditUrl(e.getId()));		return "redirect:toEdit2?id="+e.getId();	}		/**	 * 商品上架	 * @return	 * @throws Exception 	 */	@RequestMapping(value = "updateUp", method = RequestMethod.POST)	public String updateUp(String[] ids) throws Exception{		updateStatus(ids, Product.Product_status_y);		return selectList(RequestHolder.getRequest(), new Product());	}		/**	 * 商品下架	 * @return	 * @throws Exception 	 */	@RequestMapping(value = "updateDown", method = RequestMethod.POST)	public String updateDown(String[] ids) throws Exception{		updateStatus(ids, Product.Product_status_n);		return selectList(RequestHolder.getRequest(), new Product());	}		private void updateStatus(String[] ids, int status){		User user = LoginUserHolder.getLoginUser();		productService.updateProductStatus(ids,status,user.getUsername());	}		/**	 * 根据选择的商品图片名称来删除商品图片	 * @return	 * @throws IOException 	 */	@RequestMapping(value = "deleteImageByImgPaths", method = RequestMethod.POST)	public String deleteImageByImgPaths(Product e, String[] imagePaths) throws IOException{		String id = e.getId();		if(imagePaths!=null & imagePaths.length>0){			Product ee = productService.selectById(id);			if(StringUtils.isNotBlank(ee.getImages())){				String[] images = ee.getImages().split(ManageContainer.product_images_spider);				//和该商品的图片集合比对，找出不删除的图片然后保存到库				for(int i=0;i<imagePaths.length;i++){					for(int j=0;j<images.length;j++){						if(imagePaths[i].equals(images[j])){							images[j] = null;							break;						}					}					imagePaths[i] = null;				}				StringBuilder buff = new StringBuilder();				for(int j=0;j<images.length;j++){					if(images[j]!=null){						buff.append(images[j]+",");					}				}				ee.clear();				ee.setId(id);				ee.setImages(buff.toString());				if(ee.getImages().equals("")){					ee.setImages(ManageContainer.product_images_spider);//全部删除了				}				productService.update(ee);			}			imagePaths = null;		}//		getResponse().sendRedirect(getEditUrl(id));		return "redirect:toEdit2?id="+id;	}		/**	 * 设置指定的图片为产品的默认图片	 * @return	 * @throws Exception 	 *///	@Deprecated//	public String setProductImageToDefault() throws Exception{////		productService.downGoods(getIds());////		Product goods = new Product();////		String imageUrl = getRequest().getParameter("imageUrl");////		imageUrl = "upload/"+getRequest().getParameter("id")+"/"+imageUrl.substring(imageUrl.lastIndexOf("/"));//取出相对路径////		goods.setId(getRequest().getParameter("id"));////		goods.setPicture(imageUrl);////		productService.update(goods);////		getResponse().getWriter().write("0");//		return null;//	}	/**	 * 删除指定的图片	 * @return	 * @throws Exception 	 */	@Deprecated//	public String deleteImageByProductID() throws Exception{//		//项目的物理地址//		String filePath = SystemManager.getInstance().get("file_path");////		goodsService.downGoods(getIds());//		Product goods = new Product();//		String imageUrl = getRequest().getParameter("imageUrl");//		String imageName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);//		imageUrl = "upload/"+imageName;//		filePath += "\\upload\\"+getRequest().getParameter("id")+"\\"+imageName;//取出相对路径//		//		//删除图片文件//		System.out.println("filePath=="+filePath);//		File file = new File(filePath);//		if(file.exists()){//			file.delete();//		}////		FileUtils.deleteDirectory(new File(filePath));//		//		goods.setId(getRequest().getParameter("id"));////		goods.setPicture(imageUrl);//		goods = productService.selectOne(goods);//		if(goods!=null && goods.getPicture().equals(imageUrl)){//			//如果图片被设置为了封面图片，则删除//		}//		getResponse().getWriter().write("0");//		return null;//	}		/**	 * 批量生成测试用的商品	 * @return	 * @throws Exception	 * http://127.0.0.1:8080/myshop/manage/product!createTestProducts.action?_refProductID=10013&_refCatalogID=58&_refNum=33	 * http://127.0.0.1:8080/myshop/manage/product!createTestProducts.action?_refProductID=10009&_refCatalogID=28&_refNum=33	 *///	public String createTestProducts() throws Exception {//		String _refProductID = getRequest().getParameter("_refProductID");//参考商品ID//		String _refCatalogID = getRequest().getParameter("_refCatalogID");//参考类别ID//		int _refNum = Integer.valueOf(getRequest().getParameter("_refNum"));//生成数量//		if(StringUtils.isBlank(_refProductID) || StringUtils.isBlank(_refCatalogID) || _refNum<=0){//			throw new NullPointerException();//		}////		Product refp = productService.selectById(_refProductID);//		if(refp==null || StringUtils.isBlank(refp.getId())){//			throw new NullPointerException();//		}////		for(int i=0;i<_refNum;i++){//			Product product0 = new Product();//			product0.setName(refp.getName()+"_"+(i+1));//			product0.setCatalogID(refp.getCatalogID());//			product0.setPicture(refp.getPicture());//			product0.setPrice(refp.getPrice());//			product0.setNowPrice(refp.getNowPrice());//			product0.setSellcount(refp.getSellcount());//			product0.setStock(refp.getStock());//			product0.setIsnew(refp.getIsnew());//			product0.setSale(refp.getSale());//			product0.setTitle(refp.getTitle());//			product0.setDescription(refp.getDescription());//			product0.setKeywords(refp.getKeywords());//			product0.setIntroduce(refp.getIntroduce());//			product0.setImages(refp.getImages());//			product0.setProductHTML(refp.getProductHTML());//			product0.setStatus(refp.getStatus());////			productService.insert(product0);//		}//		return selectList();//	}		//	public String test2() throws IOException{//		List<Product> list = productService.selectList(new Product());//		for(int i=0;i<list.size();i++){//			Product pp = list.get(i);//			if(StringUtils.isNotBlank(pp.getProductHTML())){//				//				Product ppp = new Product();//				ppp.setId(pp.getId());//				ppp.setProductHTML(pp.getProductHTML().replace("http://jeeshopxx.oss.aliyuncs.com/", "http://myshopxx.oss.aliyuncs.com/"));//				//				logger.error(">>>test2>>"+ppp.getProductHTML());//				productService.update(ppp);//			}//		}//		//		getResponse().getWriter().write("success");//		return null;//	}		/**	 * 把所有商品的大图更新为小图	 * @return	 */	public String test10() {		logger.error("test10...");		List<Product> list = productService.selectList(new Product());		for(int i=0;i<list.size();i++){			Product pp = list.get(i);			String img = pp.getPicture();			if(StringUtils.isBlank(img)){				continue;			}			String[] arr = img.split("_");			if(arr.length==2){				String fx = img.substring(img.lastIndexOf("."));				Product p = new Product();				p.setId(pp.getId());				p.setPicture(arr[0]+"_1"+fx);								if(pp.getIsnew().toString().equals("0")){					p.setIsnew(Product.Product_isnew_n);				}else{					p.setIsnew(Product.Product_isnew_y);				}								if(pp.getSale().toString().equals("0")){					p.setSale(Product.Product_sale_n);				}else{					p.setSale(Product.Product_sale_y);				}								logger.error("p.getPicture = " + p.getPicture());				productService.updateImg(p);				//				throw new NullPointerException();			}		}				return null;	}}