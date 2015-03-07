package net.jeeshop.web.action.manage.hotquery;import net.jeeshop.core.BaseAction;import net.jeeshop.services.manage.hotquery.HotqueryService;import net.jeeshop.services.manage.hotquery.bean.Hotquery;import net.jeeshop.web.action.BaseController;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.web.bind.annotation.RequestMapping;@Controller@RequestMapping("/manage/hotquery/")public class HotqueryAction extends BaseController<Hotquery> {	private static final long serialVersionUID = 1L;	@Autowired	private HotqueryService hotqueryService;	private static final String page_toList = "/manage/hotquery/hotqueryList";	private static final String page_toEdit = "/manage/hotquery/hotqueryEdit";	private static final String page_toAdd = "/manage/hotquery/hotqueryEdit";	private HotqueryAction() {		super.page_toList = page_toList;		super.page_toAdd = page_toAdd;		super.page_toEdit = page_toEdit;	}	@Override	public HotqueryService getService() {		return hotqueryService;	}	public void setHotqueryService(HotqueryService hotqueryService) {		this.hotqueryService = hotqueryService;	}}