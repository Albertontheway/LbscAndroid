package huxibianjie.com.lbscandroid.Adapart;

import java.util.List;

public class UserNumber {

    /**
     * errcode : 0
     * errmsg : OK
     * content : {"totalElements":7,"content":[{"name":"Hehgh1","phone":"13718952453","sort":1,"money":0,"area":"北京"},{"name":"ZhGlH9","phone":"18511045311","sort":2,"money":0,"area":"北京"},{"name":"41K1Iq","phone":"15313545607","sort":3,"money":0,"area":"北京"},{"name":"g6V85D","phone":"15703993107","sort":4,"money":0,"area":"北京"},{"name":"5TVO92","phone":"15188432877","sort":5,"money":0,"area":"北京"},{"name":"Z5jNRf","phone":"15710062741","sort":6,"money":0,"area":"北京"},{"name":"tyUArW","phone":"18911000215","sort":7,"money":0,"area":"北京"}],"page":1,"pageSize":20,"totalPages":0}
     */

    private int errcode;
    private String errmsg;
    private ContentBeanX content;

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

    public ContentBeanX getContent() {
        return content;
    }

    public void setContent(ContentBeanX content) {
        this.content = content;
    }

    public static class ContentBeanX {
        /**
         * totalElements : 7
         * content : [{"name":"Hehgh1","phone":"13718952453","sort":1,"money":0,"area":"北京"},{"name":"ZhGlH9","phone":"18511045311","sort":2,"money":0,"area":"北京"},{"name":"41K1Iq","phone":"15313545607","sort":3,"money":0,"area":"北京"},{"name":"g6V85D","phone":"15703993107","sort":4,"money":0,"area":"北京"},{"name":"5TVO92","phone":"15188432877","sort":5,"money":0,"area":"北京"},{"name":"Z5jNRf","phone":"15710062741","sort":6,"money":0,"area":"北京"},{"name":"tyUArW","phone":"18911000215","sort":7,"money":0,"area":"北京"}]
         * page : 1
         * pageSize : 20
         * totalPages : 0
         */

        private int totalElements;
        private int page;
        private int pageSize;
        private int totalPages;
        private List<ContentBean> content;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class ContentBean {
            /**
             * name : Hehgh1
             * phone : 13718952453
             * sort : 1
             * money : 0
             * area : 北京
             */

            private String name;
            private String phone;
            private int sort;
            private int money;
            private String area;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }
        }
    }
}
