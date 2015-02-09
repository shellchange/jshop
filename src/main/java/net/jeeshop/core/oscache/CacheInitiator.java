package net.jeeshop.core.oscache;

import net.jeeshop.core.ManageContainer;
import net.jeeshop.core.front.SystemManager;
import net.jeeshop.services.front.advert.AdvertService;
import net.jeeshop.services.front.area.AreaService;
import net.jeeshop.services.front.attribute.AttributeService;
import net.jeeshop.services.front.catalog.CatalogService;
import net.jeeshop.services.front.comment.CommentService;
import net.jeeshop.services.front.commentType.CommentTypeService;
import net.jeeshop.services.front.express.ExpressService;
import net.jeeshop.services.front.indexImg.IndexImgService;
import net.jeeshop.services.front.keyvalue.KeyvalueService;
import net.jeeshop.services.front.navigation.NavigationService;
import net.jeeshop.services.front.news.NewsService;
import net.jeeshop.services.front.notifyTemplate.NotifyTemplateService;
import net.jeeshop.services.front.order.OrderService;
import net.jeeshop.services.front.pay.PayService;
import net.jeeshop.services.manage.systemSetting.SystemSettingService;
import net.jeeshop.services.manage.systemSetting.bean.SystemSetting;
import net.jeeshop.services.manage.accountRank.AccountRankService;
import net.jeeshop.services.manage.activity.ActivityService;
import net.jeeshop.services.manage.hotquery.HotqueryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;

/**
 * Created by dylan on 15-2-7.
 */
public class CacheInitiator {

    /**
     * manage后台
     */
//    @Autowired
//    private KeyvalueService keyvalueService;
    @Autowired
    private SystemSettingService systemSettingService;
//    @Autowired
//    private NewsService newsService;
//    @Autowired
//    private CatalogService catalogService;
//    @Autowired
//    private IndexImgService indexImgService;
//    @Autowired
//    private NavigationService navigationService;
//    @Autowired
//    private AttributeService attributeService;
//    @Autowired
//    private PayService payService;
//    @Autowired
//    private CommentTypeService commentTypeService;
//    @Autowired
//    private AreaService areaService;
//    @Autowired
//    private ExpressService expressService;
//    @Autowired
//    private AdvertService advertService;
//    @Autowired
//    private NotifyTemplateService notifyTemplateService;
//    @Autowired
//    //	private OssService ossService;
//    private OrderService orderService;
//    @Autowired
//    private CommentService commentService;
//    @Autowired
//    private AccountRankService accountRankService;
//    @Autowired
//    private ActivityService activityService;
//    @Autowired
//    private HotqueryService hotqueryService;
    /**
     * 加载系统配置信息
     */
    public void loadSystemSetting() {
        SystemManager.systemSetting = systemSettingService.selectOne(new SystemSetting());
        if (SystemManager.systemSetting == null) {
            throw new NullPointerException("未设置本地环境变量，请管理员在后台进行设置");
        }

        //从环境变量中分解出图集来。
        if (StringUtils.isNotBlank(SystemManager.systemSetting.getImages())) {
            String[] images = SystemManager.systemSetting.getImages().split(ManageContainer.product_images_spider);
            if (SystemManager.systemSetting.getImagesList() == null) {
                SystemManager.systemSetting.setImagesList(new LinkedList<String>());
            } else {
                SystemManager.systemSetting.getImagesList().clear();
            }

            for (int i = 0; i < images.length; i++) {
                SystemManager.systemSetting.getImagesList().add(images[i]);
            }
        }
    }
}
