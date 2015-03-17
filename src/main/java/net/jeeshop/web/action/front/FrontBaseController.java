package net.jeeshop.web.action.front;

import net.jeeshop.core.FrontContainer;
import net.jeeshop.core.Services;
import net.jeeshop.core.dao.page.PagerModel;
import net.jeeshop.services.front.account.bean.Account;
import net.jeeshop.web.action.front.orders.CartInfo;
import net.jeeshop.web.util.LoginUserHolder;
import net.jeeshop.web.util.RequestHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * Created by dylan on 15-3-17.
 */
@Controller
public abstract class FrontBaseController<E extends PagerModel> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public abstract Services<E> getService();
    protected static final String page_toLogin = "/account/login";
    protected static final String page_toLoginRedirect = "redirect:/account/login";

    protected Account getLoginAccount(){
        return LoginUserHolder.getLoginAccount();
    }

    protected CartInfo getMyCart(){
        return (CartInfo) RequestHolder.getSession().getAttribute(FrontContainer.myCart);
    }

}
