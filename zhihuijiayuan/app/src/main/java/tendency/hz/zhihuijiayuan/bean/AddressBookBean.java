package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Author：Libin on 2019/5/7 11:34
 * Email：1993911441@qq.com
 * Describe：
 */
public class AddressBookBean extends BaseBean<List<AddressBookBean.DataBean>>{

   public static class DataBean{
       private String name;
       private String phoneNumber;

       public String getName() {
           return name;
       }

       public DataBean(String name, String phoneNumber) {
           this.name = name;
           this.phoneNumber = phoneNumber;
       }

       public void setName(String name) {
           this.name = name;
       }

       public String getPhoneNumber() {
           return phoneNumber;
       }

       public void setPhoneNumber(String phoneNumber) {
           this.phoneNumber = phoneNumber;
       }
   }
}
