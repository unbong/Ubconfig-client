package io.unbong.ubconfig.client.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-06 10:40
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigMeata {

    String app;
    String env;
    String ns;
    String configUrl;

    public String genKey()
    {
       return app +"-" +env+"-" + ns;
    }

    public String listPath(){
        return  this.getConfigUrl() + "/list?app=" + this.getApp()
                +"&env="+this.getEnv()
                +"&ns="+this.getNs();
    }

    public String versionPath(){
        return  this.getConfigUrl() + "/version?app=" + this.getApp()
                +"&env="+this.getEnv()
                +"&ns="+this.getNs();
    }
}
