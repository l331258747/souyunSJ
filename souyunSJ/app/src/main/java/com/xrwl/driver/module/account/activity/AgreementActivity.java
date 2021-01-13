package com.xrwl.driver.module.account.activity;

import android.widget.TextView;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;

import butterknife.BindView;

public class AgreementActivity extends BaseActivity {
    @BindView(R.id.tv_content)
    TextView tv_content;


    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initViews() {
        tv_content.setText(getContent());
    }

    private String getContent() {
        String content = "隐 私 条 款\n" +
                "个人信息保护政策\n" +
                "为切实保护物流16飕云网用户隐私权，提升用户体验，山西兴荣飕云科技有限公司根据现行法规及政策，制定本《个人信息保护政策》。 本《个人信息保护政策》将详细说明物流16飕云网在获取、管理及保护用户个人信息方面的政策及措施。本《个人信息保护政策》适用于物流向您提供的所有服务。\n" +
                "个人信息的收集\n" +
                "您已知悉且同意，在您登录16飕云网网提供的服务时，16飕云将记录您提供的相关个人信息，如：姓名、手机号码等，上述个人信息是您获得16飕云提供服务的基础。同时，基于优化用户体验之目的，16飕云会获取与提升物流服务有关的其他信息。\n" +
                "个人信息的管理\n" +
                "为了向您提供更好的服务或产品，16飕云会在下述情形使用您的个人信息：\n" +
                "1）根据相关法律法规的要求；\n" +
                "2）根据您的授权；\n" +
                "3）根据物流助手网相关服务条款、应用许可使用协议的约定。\n" +
                "此外，您已知悉并同意：在现行法律法规允许的范围内，16飕云可能会将您非隐私的个人信息用于市场营销，使用方式包括但不限于：在16飕云平台中向您展示或提供广告和促销资料，向您通告或推荐物流助手网的服务或产品信息，以及其他此类根据您使用16飕云服务或产品的情况所认为您可能会感兴趣的信息。\n" +
                "未经您本人允许，物流助手网不会向任何第三方披露您的个人信息，下列情形除外：\n" +
                "1）16飕云已经取得您或您监护人或代您制作请柬的制作人的授权；\n" +
                "2）司法机关或行政机关给予法定程序要求物流助手网披露的；\n" +
                "3）16飕云为维护自身合法权益而向用户提起诉讼或仲裁时；\n" +
                "4）根据您与16飕云相关服务条款、应用许可使用协议的约定；\n" +
                "5）法律法规规定的其他情形。\n" +
                "个人信息的保护\n" +
                "16飕云将尽一切合理努力保护其获得的用户个人信息。为防止用户个人信息在意外的、未经授权的情况下被非法访问、复制、修改、传送、遗失、破坏、处理或使用，16飕云已经并将继续采取以下措施保护您的个人信息：\n" +
                "1）以适当的方式对用户的个人信息进行加密处理；\n" +
                "2）限制对用户个人信息的访问；\n" +
                "3）其他的合理措施。\n" +
                "尽管已经采取了上述合理有效措施，并已经遵守了相关法律规定要求的标准，但16飕云仍然无法保证您的个人信息通过不安全途径进行交流时的安全性。因此，用户个人应采取积极措施保证个人信息的安全，如：定期修改帐号密码，不将自己的帐号密码等个人信息透露给他人。\n" +
                "您知悉：16飕云提供的个人信息保护措施仅适用于16飕云平台，一旦您离开16飕云，浏览或使用其他网站、服务及内容资源，16飕云即没有能力及义务保护您在16飕云以外的网站提交的任何个人信息，无论您登陆或浏览上述网站是否基于16飕云的链接或引导。\n" +
                "个人信息的访问\n" +
                "当您完成16飕云的帐号注册后，您可以查阅或修改您提交给16飕云的个人信息。一般情况下，您可随时浏览、修改自己提交的信息，但出于安全性和身份识别（如号码申诉服务）的考虑，您可能无法修改注册时提供的某些初始注册信息及验证信息。\n" +
                "关于免责说明\n" +
                "就下列相关事宜的发生，物流助手网不承担任何法律责任：\n" +
                "* 由于您将用户密码告知他人或与他人共享注册帐户，由此导致的任何个人信息的泄露，或其他非因16飕云原因导致的个人信息的泄露；\n" +
                "* 16飕云根据法律规定或政府相关政策要求提供您的个人信息；\n" +
                "* 任何第三方根据16飕云各服务条款及声明中所列明的情况使用您的个人信息，由此所产生的纠纷；\n" +
                "* 任何由于黑客攻击、电脑病毒侵入或政府管制而造成的暂时性网站关闭；\n" +
                "* 因不可抗力导致的任何后果；\n" +
                "* 物流助手网在各服务条款及声明中列明的使用方式或免责情形。\n" +
                "隐私保护政策的修改\n" +
                "16飕云有权随时修改《个人信息保护政策》的任何条款，一旦《个人信息保护政策》的内容发生变动，16飕云将会直接在微博网站上公布修改之后的《个人信息保护政策》，该公布行为视为16飕云已经通知您修改内容。16飕云也可通过其他适当方式向用户提示修改内容。如果您不同意16飕云对本《个人信息保护政策》相关条款所做的修改，您有权停止使用物流助手网服务。如果您继续使用物流助手网服务，则视为您接受山西兴荣飕云科技有限公司对本协议相关条款所做的修改。\n";
        return content;
    }
}
