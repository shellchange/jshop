package net.jeeshop.core.freemarker.fn;

import com.opensymphony.xwork2.ActionContext;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import net.jeeshop.core.ManageContainer;
import net.jeeshop.core.front.SystemManager;
import org.apache.struts.action.ActionServlet;

import java.util.List;

/**
 * 获取当前登录的用户
 * Created by dylan on 15-1-15.
 */
public class CurrentUserGetter implements TemplateMethodModelEx {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        return ActionContext.getContext().getSession().get(ManageContainer.manage_session_user_info);
    }
}
