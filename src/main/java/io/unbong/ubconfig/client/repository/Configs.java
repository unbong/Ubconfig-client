package io.unbong.ubconfig.client.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configs model
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-06 10:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configs {

    private String app;
    private String env;
    private String ns;
    private String pkey;
    private String pval;
}
