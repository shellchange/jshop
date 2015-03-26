package net.jeeshop.web.action.manage.notifyTemplate;import com.alibaba.fastjson.JSON;import net.jeeshop.core.util.FreemarkerTemplateUtil;import net.jeeshop.services.manage.notifyTemplate.NotifyTemplateService;import net.jeeshop.services.manage.notifyTemplate.bean.NotifyTemplate;import net.jeeshop.web.action.BaseController;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.web.bind.annotation.ModelAttribute;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.ResponseBody;import org.tuckey.web.filters.urlrewrite.utils.StringUtils;import javax.servlet.http.HttpServletRequest;import java.io.IOException;import java.util.HashMap;import java.util.List;import java.util.Map;/** * 通知模板 * @author jqsl2012@163.com * @author dylan * */@Controller@RequestMapping("/manage/notifyTemplate/")public class NotifyTemplateAction extends BaseController<NotifyTemplate> {	private static final Logger logger = LoggerFactory.getLogger(NotifyTemplateAction.class);	private static final long serialVersionUID = 1L;	@Autowired	private NotifyTemplateService notifyTemplateService;	private static final String page_toList = "/manage/notifyTemplate/notifyTemplateList";	private NotifyTemplateAction() {		super.page_toList = page_toList;		super.page_toAdd = null;		super.page_toEdit = null;	}		@Override	public NotifyTemplateService getService() {		return notifyTemplateService;	}	public void setNotifyTemplateService(			NotifyTemplateService notifyTemplateService) {		this.notifyTemplateService = notifyTemplateService;	}	public void insertAfter(NotifyTemplate e) {		e.clear();	}		@Override	public String selectList(HttpServletRequest request, @ModelAttribute("e") NotifyTemplate e) throws Exception {		super.initPageSelect();		List<NotifyTemplate> notifyTemplateList = notifyTemplateService.selectList(new NotifyTemplate());//		if(notifyTemplateList!=null && notifyTemplateList.size()>0){//			for(int i=0;i<){//				//			}//		}		request.setAttribute("notifyTemplateList", notifyTemplateList);		return page_toList;	}		/**	 * 修改模板	 * @throws IOException 	 *///	public String updateTemplate() throws IOException{//		logger.error("updateTemplate...");//		logger.error("updateTemplate...e="+e.toString());//		if(StringUtils.isBlank(e.getCode()) || StringUtils.isBlank(e.getTemplate())){//			getResponse().getWriter().write("-1");//保存失败，参数不能为空！//		}else{//			getServer().update(getE());//			getResponse().getWriter().write("0");//保存成功//		}//		return null;//	}		@Override	public String update(HttpServletRequest request, NotifyTemplate e) throws Exception {		logger.error("update...");		logger.error("update...e="+e.toString());		e.setTemplateCheckError(null);		if(StringUtils.isBlank(e.getCode()) || StringUtils.isBlank(e.getTemplate())){			throw new NullPointerException();		}		getService().update(e);				//验证模板是否可用		if(NotifyTemplate.email_reg.equals(e.getCode())){			Map data = new HashMap();  			data.put("nickname", "测试");			data.put("system", "jeeshop");			data.put("url", "http://www.baidu.com");			data.put("servicesPhone", "400-666-8888");			data.put("systemEmail", "jeeshop@jeeshop.net");			data.put("helpUrl", "http://www.baidu.com");			try {				FreemarkerTemplateUtil.freemarkerProcess(data,e.getTemplate());			} catch (Exception e1) {				e1.printStackTrace();				e.setTemplateCheckError("模板验证未通过！请检查！");			}		}		return selectList(request, e);	}		/**	 * 根据code查询指定的模板内容-ajax	 * @return	 * @throws IOException	 */	@RequestMapping("selectTemplateByCode")	@ResponseBody	public NotifyTemplate selectTemplateByCode(NotifyTemplate e) throws IOException{		if(StringUtils.isBlank(e.getCode())){			throw new NullPointerException("code不能为空！");		}				NotifyTemplate ee = new NotifyTemplate();		ee.setCode(e.getCode());		ee = notifyTemplateService.selectOne(ee);		String json = JSON.toJSONString(ee);		logger.error("selectTemplateByCode.jspn = " + json);		return ee;	}}