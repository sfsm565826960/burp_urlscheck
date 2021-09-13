/**
 * Author: Franchen
 * Date: 2021/09/10
 */
package burp;

import java.awt.Component;
import java.io.PrintWriter;
import javax.swing.*;

public class BurpExtender implements IBurpExtender, IHttpListener, ITab
{
    private IBurpExtenderCallbacks callbacks;
    private static IExtensionHelpers helpers;
    private static PrintWriter stdout;
    private Targets targets;
    public MainUI mainUI;

    
    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks)
    {
        this.callbacks = callbacks;
        this.targets = new Targets(100);
        BurpExtender.helpers = callbacks.getHelpers();
        BurpExtender.stdout = new PrintWriter(callbacks.getStdout(), true);

        // set our extension name
        callbacks.setExtensionName("UrlsCheck");

        stdout.println("Name: UrlsCheck\nAuthor: Franchen");

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                // customize our UI components
                mainUI = new MainUI(targets);
                callbacks.customizeUiComponent(mainUI);
                callbacks.addSuiteTab(BurpExtender.this);
            }
        });
        callbacks.registerHttpListener(BurpExtender.this);
    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo)
    {
        // 仅标记PROXY
        if (toolFlag ==4 && messageIsRequest) {
            String url = helpers.analyzeRequest(messageInfo.getHttpService(), messageInfo.getRequest()).getUrl().toString();
            // 过滤后缀
            if (!targets.isExcludeSuffix(url)) {
                // 筛选目标
                if (targets.contains(url)) {
                    mainUI.updateModel(targets.getArray());
                    messageInfo.setHighlight("blue");
                }
            }
        }
    }

    @Override
    public String getTabCaption() {
        return "UrlsCheck";
    }

    @Override
    public Component getUiComponent() {
        return mainUI;
    }

    public static void main(String[] args) {
        System.out.println("UrlsCheck product by Franchen");
    }
}