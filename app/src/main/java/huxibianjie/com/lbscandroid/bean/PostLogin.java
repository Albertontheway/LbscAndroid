package huxibianjie.com.lbscandroid.bean;

/**
 * Created by Mr.c on 2018/5/16.
 */

public class PostLogin {


    /**
     * errcode : 0
     * errmsg : OK
     * content : {"seckey":"eeXiGmaozdP3","phone":"15313545607"}
     */

    private int errcode;
    private String errmsg;
    private ContentBean content;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * seckey : eeXiGmaozdP3
         * phone : 15313545607
         */

        private String seckey;
        private String phone;

        public String getSeckey() {
            return seckey;
        }

        public void setSeckey(String seckey) {
            this.seckey = seckey;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}