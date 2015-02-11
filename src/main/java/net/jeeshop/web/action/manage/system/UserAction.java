package net.jeeshop.web.action.manage.system;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.jeeshop.core.ManageContainer;
import net.jeeshop.core.Services;
import net.jeeshop.core.dao.page.PagerModel;
import net.jeeshop.core.oscache.ManageCache;
import net.jeeshop.core.system.bean.User;
import net.jeeshop.core.util.AddressUtils;
import net.jeeshop.core.util.MD5;
import net.jeeshop.services.manage.system.impl.RoleService;
import net.jeeshop.services.manage.system.impl.UserService;
import net.jeeshop.services.manage.systemlog.SystemlogService;
import net.jeeshop.services.manage.systemlog.bean.Systemlog;

import net.jeeshop.web.action.BaseController;
import net.jeeshop.web.util.LoginUserHolder;
import net.jeeshop.web.util.RequestHolder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 后台用户管理
 * @author huangf
 * @author dylan
 *
 */
@Controller
@RequestMapping("/manage/user")
public class UserAction extends BaseController<User> {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserAction.class);

	private static final long serialVersionUID = 1L;

    private static final String page_input = "/manage/system/index";
    private static final String page_home = "/manage/main";
    private static final String page_toList = "/manage/system/user/userList";
    private static final String page_toAdd = "/manage/system/user/editUser";
    private static final String page_toEdit = "/manage/system/user/editUser";
    private static final String page_toChangePwd = "/manage/system/user/toChangePwd";
    private static final String page_changePwd = "/manage/system/user/changePwd";
    private static final String page_show = "/manage/system/user/show";
    private static final String page_initManageIndex = "/manage/system/right";
    public UserAction() {
        super.page_toList = page_toList;
    }
    @Autowired
	private UserService userService;
    @Autowired
	private RoleService roleService;
    @Resource(name = "systemlogServiceManage")
	private SystemlogService systemlogService;
    @Resource
	private ManageCache manageCache;

    @Override
    public Services<User> getService() {
        return userService;
    }

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ManageCache getManageCache() {
		return manageCache;
	}

	public void setManageCache(ManageCache manageCache) {
		this.manageCache = manageCache;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	public void insertAfter(User e) {
		e.clear();
	}
	public void setSystemlogService(SystemlogService systemlogService) {
		this.systemlogService = systemlogService;
	}
	
//	@Override
//	public void prepare() throws Exception {
//		if(this.e==null){
//			this.e = new User();
//		}
//
//		super.initPageSelect();
//	}
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(@ModelAttribute("e") User e){
        return page_input;
    }

	/**
	 * 后台登录
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(HttpSession session, @ModelAttribute("errorMsg") String errorMsg,@ModelAttribute("e") User e) throws Exception {

		if (session.getAttribute(ManageContainer.manage_session_user_info) != null) {
			return "redirect:/manage/user/home";
		}
		
		errorMsg = "<font color='red'>帐号或密码错误!</font>";
		if (StringUtils.isBlank(e.getUsername()) || StringUtils.isBlank(e.getPassword())){
			session.setAttribute(ManageContainer.loginError, "账户和密码不能为空!");
			return page_input;
		}
		
		e.setPassword(MD5.md5(e.getPassword()));
		User u = ((UserService)getService()).login(e);
		if (u == null) {
			logger.error("登陆失败，账号不存在！");
			session.setAttribute(ManageContainer.loginError, errorMsg);
			return page_input;
		}else if(!u.getStatus().equals(User.user_status_y)){
			logger.error("帐号已被禁用，请联系管理员!");
			errorMsg = "<font color='red'>帐号已被禁用，请联系管理员!</font>";
            session.setAttribute(ManageContainer.loginError, errorMsg);
			return page_input;
		}
		u.setUsername(e.getUsername());
		errorMsg = null;
		e.clear();
		session.setAttribute(ManageContainer.manage_session_user_info, u);
		
		//解析用户的数据库权限，以后可以进行DB权限限制
		if(StringUtils.isNotBlank(u.getRole_dbPrivilege())){
			String[] dbPriArr = u.getRole_dbPrivilege().split(",");
			if(u.getDbPrivilegeMap()==null){
				u.setDbPrivilegeMap(new HashMap<String, String>());
			}else{
				u.getDbPrivilegeMap().clear();
			}
			
			if(dbPriArr.length!=0){
				for(int i=0;i<dbPriArr.length;i++){
					u.getDbPrivilegeMap().put(dbPriArr[i], dbPriArr[i]);
				}
			}
		}
		
		try {
			loginLog(u,"login");
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		
		return "redirect:/manage/user/home";
	}
    @RequestMapping("home")
    public String home(){
        if(LoginUserHolder.getLoginUser() == null){
            return "redirect:/manage/user/login";
        }
        return page_home;
    }
	
	private void loginLog(User u,String log) {
		Systemlog systemlog = new Systemlog();
		systemlog.setTitle(log);
		systemlog.setContent(log);
		systemlog.setAccount(u.getUsername());
		systemlog.setType(1);
		systemlog.setLoginIP(AddressUtils.getIp(RequestHolder.getRequest()));
		
		String address = null;
		if(!systemlog.getLoginIP().equals("127.0.0.1") && !systemlog.getLoginIP().equals("localhost")){
			//获取指定IP的区域位置
			try {
				address = AddressUtils.getAddresses("ip=" + systemlog.getLoginIP(), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			systemlog.setLoginArea(address);
			
			//异地登陆的判断方法为：先比较本次登陆和上次登陆的区域位置，如果不一致，说明是异地登陆；如果获取不到区域，则比较IP地址，如果IP地址和上次的不一致，则是异地登陆
			Systemlog firstSystemlog = systemlogService.selectFirstOne(u.getUsername());
			if(firstSystemlog!=null){
				if(StringUtils.isNotBlank(address) && StringUtils.isNotBlank(firstSystemlog.getLoginArea())){
					if(!address.equals(firstSystemlog.getLoginArea())){
						systemlog.setDiffAreaLogin(Systemlog.systemlog_diffAreaLogin_y);
					}
				}else if(StringUtils.isNotBlank(systemlog.getLoginIP()) && StringUtils.isNotBlank(firstSystemlog.getLoginIP())){
					if(!systemlog.getLoginIP().equals(firstSystemlog.getLoginIP())){
						systemlog.setDiffAreaLogin(Systemlog.systemlog_diffAreaLogin_y);
					}
				}
			}
		}
		
		systemlogService.insert(systemlog);
	}
	
	/**
	 * 添加用户
	 */
    @Override
    @RequestMapping("insert")
	public String insert(HttpServletRequest request, @ModelAttribute("e") User user) throws Exception {
		return save0(user);
	}

	/**
	 * 修改用户信息
	 */
    @Override
    @RequestMapping("update")
	public String update(HttpServletRequest request, @ModelAttribute("e") User user) throws Exception {
		return save0(user);
	}

	private String save0(User e) throws Exception {
		logger.error("save0..."+e.getPassword()+","+e.getNewpassword2());
		
		if(StringUtils.isBlank(e.getId())){//添加
			if(StringUtils.isBlank(e.getPassword()) || StringUtils.isBlank(e.getNewpassword2())){
				throw new NullPointerException("输入的密码不符合要求！");
			}
			
			if(!e.getPassword().equals(e.getNewpassword2())){
				throw new IllegalArgumentException("两次输入的密码不一致！");
			}
			
			User user = (User)RequestHolder.getSession().getAttribute(ManageContainer.manage_session_user_info);
			e.setCreateAccount(user.getUsername());
			if(StringUtils.isBlank(e.getStatus())){
				e.setStatus(User.user_status_y);
			}
			e.setPassword(MD5.md5(e.getPassword()));
			getService().insert(e);
		}else{//修改
			
			//当前登录用户是admin，才能修改admin的信息，其他用户修改admin信息都属于非法操作。
			User user = (User)RequestHolder.getSession().getAttribute(ManageContainer.manage_session_user_info);
			if(!user.getUsername().equals("admin") && e.getUsername().equals("admin")){
				throw new RuntimeException("操作非法！");
			}
			
			if(StringUtils.isBlank(e.getPassword()) && StringUtils.isBlank(e.getNewpassword2())){
				//不修改密码
				e.setPassword(null);
			}else{
				//修改密码
				if(!e.getPassword().equals(e.getNewpassword2())){
					throw new IllegalArgumentException("两次输入的密码不一致！");
				}
				e.setPassword(MD5.md5(e.getPassword()));
			}
			
			e.setUpdateAccount(user.getUsername());
			getService().update(e);
		}
		return back(RequestHolder.getRequest(), e);
	}

    /**
     * 注销登录
     * @return
     * @throws Exception
     */
    @RequestMapping("loginOut")
	public String loginOut(@ModelAttribute("e") User e) throws Exception {
		User u = (User) RequestHolder.getSession().getAttribute(ManageContainer.manage_session_user_info);
		if(u!=null && StringUtils.isNotBlank(u.getUsername())){
			loginLog(u,"loginOut");
		}

        RequestHolder.getSession().setAttribute(ManageContainer.manage_session_user_info,null);
        RequestHolder.getSession().setAttribute(ManageContainer.resource_menus,null);
        RequestHolder.getSession().setAttribute(ManageContainer.user_resource_menus_button,null);
		e.clear();
		return page_input;
	}

	/**
	 * ajax验证输入的字符的唯一性
	 * @return
	 * @throws IOException
	 */
    @RequestMapping("unique")
    @ResponseBody
	public String unique(@ModelAttribute("e") User e, HttpServletResponse response) throws IOException{
		logger.error("验证输入的字符的唯一性"+e);
        response.setCharacterEncoding("utf-8");
		synchronized (this) {
			if(StringUtils.isNotBlank(e.getNickname())){//验证昵称是否被占用
				logger.error("验证昵称是否被占用");
				User user = new User();
				user.setNickname(e.getNickname());
				
//				if(userService.selectCount(e)>0){
//					getResponse().getWriter().write("{\"error\":\"昵称已经被占用!\"}");
//				}else{
//					getResponse().getWriter().write("{\"ok\":\"昵称可以使用!\"}");
//				}
				
				user = userService.selectOneByCondition(user);
				
				if(user==null){
					//数据库中部存在此编码
                    return "{\"ok\":\"昵称可以使用!\"}";
				}else{
					if(StringUtils.isBlank(e.getId())){
						//当前为insert操作，但是编码已经存在，则只可能是别的记录的编码
						return "{\"error\":\"昵称已经存在!\"}";
					}else{
						//update操作，又是根据自己的编码来查询的，所以当然可以使用啦
						return "{\"ok\":\"昵称可以使用!\"}";
					}
				}
			}else if(StringUtils.isNotBlank(e.getUsername())){//验证用户名是否被占用
				logger.error("验证账号是否被占用");
				User user = new User();
				user.setUsername(e.getUsername());
				if(userService.selectCount(user)>0){
					return "{\"error\":\"账号已经被占用!\"}";
				}else{
					return "{\"ok\":\"账号可以使用!\"}";
				}
			}
		}
		return null;
	}
//	@Override
//	protected void toEditBefore(User e) {
//		String id = getRequest().getParameter("id");
//		if (id!=null) {
//			e.clear();
//			e.setId(id);
//			e = getServer().selectOne(e);
//		}else{
//			e.clear();
//		}
//	}
	
	/**
	 * 转到修改密码页面
	 * @return
	 */
    @RequestMapping("toChangePwd")
	public String toChangePwd(HttpSession session, @ModelAttribute("e") User e){
		User u = (User) session.getAttribute(ManageContainer.manage_session_user_info);
		e.setId(u.getId());
		return page_toChangePwd;
	}
	
	/**
	 * 修改密码
	 * @return
	 */
    @RequestMapping("updateChangePwd")
	public String updateChangePwd(@ModelAttribute("errorMsg")String errorMsg, @ModelAttribute("e") User e){
		errorMsg = "两次输入的密码不一致，修改密码失败!";
		if(StringUtils.isBlank(e.getNewpassword()) || StringUtils.isBlank(e.getNewpassword2())){
			throw new NullPointerException("密码不能为空！");
		}
		
		if(!e.getNewpassword().equals(e.getNewpassword2())){
			throw new IllegalArgumentException("两次输入的密码不一致！");
		}
		
		errorMsg = "旧密码输入错误，修改密码失败!";
		
		User u = (User) RequestHolder.getSession().getAttribute(ManageContainer.manage_session_user_info);
		e.setPassword(MD5.md5(e.getPassword()));
		if(!e.getPassword().equals(u.getPassword())){//用户输入的旧密码和数据库中的密码一致
			throw new IllegalArgumentException("原密码不正确！");
		}
		
		//修改密码
		e.setPassword(MD5.md5(e.getNewpassword()));
		this.getService().update(e);
		errorMsg = "密码修改成功!";
		
		return page_changePwd;
	}
	

    @RequestMapping("toAdd")
	public String toAdd(@ModelAttribute("e")User user, ModelMap model) throws Exception {
        model.addAttribute("roleList", roleService.selectList(null));
		return page_toAdd;
	}
	@Override
	protected void selectListAfter(PagerModel pager) {
		pager.setPagerUrl("user/selectList");
	}
	
	/**
	 * 编辑用户
	 */
    @RequestMapping("toEdit")
	public String toEdit(@ModelAttribute("e") User e, ModelMap model) throws Exception {
        model.addAttribute("roleList", roleService.selectList(null));

		e = getService().selectOne(e);
//		if(getRequest().getParameter("id")==null){
//			e.clear();
//		}else{
//			e.setId(getRequest().getParameter("id"));
//			e = getServer().selectOne(e);
//		}
		
		return page_toEdit;
	}
	
	/**
	 * 查看管理人员信息
	 * @return
	 */
    @RequestMapping("show")
	public String show(@ModelAttribute("e") User e, String account){
		if(StringUtils.isBlank(account)){
			throw new NullPointerException("非法请求！");
		}
		
		e.setUsername(account);
		e = getService().selectOne(e);
		return page_show;
	}

	/**
	 * 用户修改密码--验证旧密码是否正确
	 * @return
	 */
    @RequestMapping("checkOldPassword")
    @ResponseBody
	public String checkOldPassword(@ModelAttribute("e")User e, HttpSession session) throws Exception{
		logger.error("checkOldPassword.."+e.getPassword());
		if(StringUtils.isBlank(e.getPassword())){
			return "{\"error\":\"旧密码不能为空!\"}";
		}else{
			//检查旧密码输入的是否正确
			User user = (User)session.getAttribute(ManageContainer.manage_session_user_info);
			String oldPass = MD5.md5(e.getPassword());
			if(user.getPassword().equals(oldPass)){
				return "{\"ok\":\"旧密码输入正确!\"}";
			}else{
				return "{\"error\":\"旧密码输入错误!\"}";
			}
		}
	}

	/**
	 * 加载后台首页数据
	 * @return
	 */
    @RequestMapping("initManageIndex")
	public String initManageIndex(){
		//店主每次登陆后台都需要加载综合统计数据？！还是说每次都触发加载，但是到底加载不加载具体看系统的加载策略？！
		manageCache.loadOrdersReport();
		return page_initManageIndex;
	}
    @Override
    @RequestMapping("deletes")
    public String deletes(HttpServletRequest request, String[] ids, @ModelAttribute("e") User e) throws Exception{
        throw new RuntimeException("not support");
    }
}
