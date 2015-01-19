package net.jeeshop.core.freemarker.fn;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import net.jeeshop.core.ManageContainer;
import net.jeeshop.core.PrivilegeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by dylan on 15-1-19.
 */
public class PrivilegeChecker implements TemplateMethodModelEx {
    private static Logger logger = LoggerFactory.getLogger(PrivilegeChecker.class);
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if(arguments == null || arguments.size() == 0){
            return true;
        }
        if(!(arguments.get(0) instanceof String)){
            return true;
        }
        String res = (String)arguments.get(0);
        if(StringUtils.isBlank(res)){
            return true;
        }
        HttpSession session = ServletActionContext.getRequest().getSession(false);
        logger.info("check privilege ,res : {}, session id :{}", res, session == null ? null : session.getId());
        return PrivilegeUtil.check(session, res);
    }
}
