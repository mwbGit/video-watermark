package com.harry.videowatermark.model;

import lombok.Data;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/12/4
 */
@Data
public class WaterOrderDetail {
    private String transaction_id;
    private String quantity;
    private String original_transaction_id;
    private String subscription_group_identifier;
    private String purchase_date_pst;
    private String original_purchase_date_ms;
    private String is_in_intro_offer_period;
    private String expires_date;
    private String original_purchase_date_pst;
    private String is_trial_period;
    private String expires_date_pst;
    private String original_purchase_date;
    private String expires_date_ms;
    private String purchase_date_ms;
    private String product_id;
    private String purchase_date;
    private String web_order_line_item_id;
}