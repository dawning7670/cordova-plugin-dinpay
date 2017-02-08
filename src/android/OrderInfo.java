package cordova.plugin.dinpay;

import android.text.TextUtils;

import com.itrus.util.sign.RSAWithSoftware;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 传递给智付插件参数实体
 */
public class OrderInfo {

    /** 商家号**/
    private String merchant_code;
    /** 服务器异步通知地址*/
    private String notify_url;
    /** 接口版本*/
    private String interface_version;
    /** 签名方式*/
    private String sign_type;
    /** 商户网站唯一订单号*/
    private String order_no;
    /** 商户订单时间     格式yyyy-MM-dd HH:mm:ss*/
    private String order_time;
    /** 商户订单总金额    以元为单位，精确到小数点后两位*/
    private String order_amount;
    /** 商品名称*/
    private String product_name;
    /** 订单是否允许重复标识  可选*/
    private String redo_flag;
    /** 商品编号   可选*/
    private String product_code;
    /** 商品数量   可选*/
    private String product_num;
    /** 商品描述   可选*/
    private String product_desc;
    /** 公用回传参数    可选*/
    private String extra_return_param;
    /** 消费者姓名 可选 必须和身份证号一起用 **/
    private String customer_name;
    /** 消费者身份证号 可选 必须与姓名一起用  **/
    private String customer_idNumber;
    /**公用业务扩展参数*/
    private String extend_param;

    public OrderInfo() {
    }

    public OrderInfo(JSONObject jsonObject) throws JSONException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.merchant_code = jsonObject.getString("merchant_code");
        this.notify_url = jsonObject.getString("notify_url");
        this.interface_version = jsonObject.getString("interface_version");
        this.sign_type = "RSA-S";
        this.order_no = jsonObject.getString("order_no");
        this.order_time = format.format(new Date());
        this.order_amount = jsonObject.getString("order_amount");
        this.product_name = jsonObject.getString("product_name");
        this.redo_flag = jsonObject.getString("redo_flag");
        this.product_code = jsonObject.getString("product_code");
        this.product_num = jsonObject.getString("product_num");
        this.product_desc = jsonObject.getString("product_desc");
        this.extra_return_param = jsonObject.getString("extra_return_param");
        this.customer_name = jsonObject.getString("customer_name");
        this.customer_idNumber = jsonObject.getString("customer_idNumber");
        this.extend_param = cordova.plugin.dinpay.DigestUtils.toMosaic(customer_name, customer_idNumber);
    }

    /**
     * 签名并转化成xml
     * @return
     */
    public String toXML() throws Exception {
        String sign = sign();
        return toXML(sign);
    }

    /**
     * 对字段进行签名
     * @return
     * @throws Exception
     */
    private String sign() throws Exception {
        String rawSign = getRawSign();
        String sign = getRSASSignature(rawSign);
        return sign;
    }

    /**
     * 将签名带入 生成xml报文
     * @param sign
     * @return
     */
    private String toXML(String sign) {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "\n" +
                "<dinpay>\n" +
                "  <request>\n" +
                "    <merchant_code>" + this.merchant_code + "</merchant_code>\n" +
                "    <notify_url>" + this.notify_url + "</notify_url>\n" +
                "    <interface_version>" + this.interface_version + "</interface_version>\n" +
                "    <sign_type>" + this.sign_type + "</sign_type>\n" +
                "    <sign>" + sign + "</sign>\n" +
                "    <trade>\n" +
                "      <order_no>" + this.order_no + "</order_no>\n" +
                "      <order_time>" + this.order_time + "</order_time>\n" +
                "      <order_amount>" + this.order_amount + "</order_amount>\n" +
                "      <product_name>" + this.product_name + "</product_name>\n" +
                "      <redo_flag>" + this.redo_flag + "</redo_flag>\n" +
                "      <product_code>" + this.product_code + "</product_code>\n" +
                "      <product_num>" + this.product_num + "</product_num>\n" +
                "      <product_desc>" + this.product_desc + "</product_desc>\n" +
                "      <extra_return_param>" + this.extra_return_param + "</extra_return_param>\n" +
                "      <extend_param>" + this.extend_param + "</extend_param>\n" +
                "    </trade>\n" +
                "  </request>\n" +
                "</dinpay>\n";
        return xml;
    }

    /**
     * 获取签名串
     * @return
     */
    private String getRawSign() {
        //组织签名规则格式
        Map<String, String> maps = new TreeMap<String, String>();
        maps.put("merchant_code", this.merchant_code);
        maps.put("notify_url", this.notify_url);
        maps.put("interface_version", this.interface_version);
        maps.put("order_no", this.order_no);
        maps.put("order_time", this.order_time);
        maps.put("order_amount", this.order_amount);
        maps.put("product_name", this.product_name);
        maps.put("redo_flag", this.redo_flag);
        maps.put("product_code", this.product_code);
        maps.put("product_num", this.product_num);
        maps.put("product_desc", this.product_desc);
        maps.put("extra_return_param", this.extra_return_param);
        maps.put("extend_param", this.extend_param);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            if (!TextUtils.isEmpty(value)) {
                sb.append(entry.getKey() + "=" + value + "&");
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * RSA签名
     * @param plainText
     * @return
     * @throws Exception
     */
    private String getRSASSignature(String plainText) throws Exception {
        /**
         1)merchant_private_key，商户私钥，商户按照《密钥对获取工具说明》操作并获取商户私钥；获取商户私钥的同时，也要获取商户公钥（merchant_public_key）；调试运行
         代码之前首先先将商户公钥上传到智付商家后台"支付管理"->"公钥管理"（如何获取和上传请查看《密钥对获取工具说明》），不上传商户公钥会导致调试运行代码时报错。
         2)demo提供的merchant_private_key是测试商户号1111110166的商户私钥，请自行获取商户私钥并且替换	*/

        String merchant_private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALf/+xHa1fDTCsLYPJLHy80aWq3djuV1T34sEsjp7UpLmV9zmOVMYXsoFNKQIcEzei4QdaqnVknzmIl7n1oXmAgHaSUF3qHjCttscDZcTWyrbXKSNr8arHv8hGJrfNB/Ea/+oSTIY7H5cAtWg6VmoPCHvqjafW8/UP60PdqYewrtAgMBAAECgYEAofXhsyK0RKoPg9jA4NabLuuuu/IU8ScklMQIuO8oHsiStXFUOSnVeImcYofaHmzIdDmqyU9IZgnUz9eQOcYg3BotUdUPcGgoqAqDVtmftqjmldP6F6urFpXBazqBrrfJVIgLyNw4PGK6/EmdQxBEtqqgXppRv/ZVZzZPkwObEuECQQDenAam9eAuJYveHtAthkusutsVG5E3gJiXhRhoAqiSQC9mXLTgaWV7zJyA5zYPMvh6IviX/7H+Bqp14lT9wctFAkEA05ljSYShWTCFThtJxJ2d8zq6xCjBgETAdhiH85O/VrdKpwITV/6psByUKp42IdqMJwOaBgnnct8iDK/TAJLniQJABdo+RodyVGRCUB2pRXkhZjInbl+iKr5jxKAIKzveqLGtTViknL3IoD+Z4b2yayXg6H0g4gYj7NTKCH1h1KYSrQJBALbgbcg/YbeU0NF1kibk1ns9+ebJFpvGT9SBVRZ2TjsjBNkcWR2HEp8LxB6lSEGwActCOJ8Zdjh4kpQGbcWkMYkCQAXBTFiyyImO+sfCccVuDSsWS+9jrc5KadHGIvhfoRjIj2VuUKzJ+mXbmXuXnOYmsAefjnMCI6gGtaqkzl527tw=";
        String signData = RSAWithSoftware.signByPrivateKey(plainText, merchant_private_key);
        signData = signData.replaceAll("\\+", "%2B");
        return signData;
    }


    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getInterface_version() {
        return interface_version;
    }

    public void setInterface_version(String interface_version) {
        this.interface_version = interface_version;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getRedo_flag() {
        return redo_flag;
    }

    public void setRedo_flag(String redo_flag) {
        this.redo_flag = redo_flag;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getExtra_return_param() {
        return extra_return_param;
    }

    public void setExtra_return_param(String extra_return_param) {
        this.extra_return_param = extra_return_param;
    }

    public String getCustomer_idNumber() {
        return customer_idNumber;
    }

    public void setCustomer_idNumber(String customer_idNumber) {
        this.customer_idNumber = customer_idNumber;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
}
