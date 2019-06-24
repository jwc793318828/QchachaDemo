
public class AliResultBean {
	  /**
     * success : true
     * result : {"csessionid":"01d9Yjdv4DQmXwMpb3MvzWQK5ylmdTssBuVaII62jGHWp8ijeHKzmVsBb5h0VNGKUw0guSwCyL-M6JiICtRAJzB0Hzrwy86Xy1xTAvWk_gKd7Weka3UsK301w5Bnjtu_IIiNLx2-CSvRRsVMt8QQy3fY3qimr-_g_w-m3mW4nE4pY","code":0,"value":"05JkRFK6UtwzBZKy3RJfMYNAIWwrzhijKQH-kvPlpybABNrfF-81bdYmU1cAk0dY6TPxK_HUqjZs3jLvw9uOjvzopOgO3vcDdC1oDvQo1Hyip3-OAKoIicaJiUZx1DNx-6f8OK_vIrbixjKx0P2veOw5kYBtxP2svA7zDwMJSgX4-somcC00Xc3bB7XRNv01vAU74wdbrQ09SHVCzLJeBRoLMffyTDhVZsgAYWofHI7D4yeseffrcKo5dMHZ9qBCLuZcfo8yWDlc5cohEtSNOPWNQdADaoRT3Zh9q8w-j2AJPMaTfKfpo13slkAfHVKSeDr1moSb1uZw865_6bs5NWlxTHyO8yXO-cpLPHk6IOiglZ4gJpjtaUY30OUWw0w0Zq"}
     */

    private boolean success;
    private ResultBean result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * csessionid : 01d9Yjdv4DQmXwMpb3MvzWQK5ylmdTssBuVaII62jGHWp8ijeHKzmVsBb5h0VNGKUw0guSwCyL-M6JiICtRAJzB0Hzrwy86Xy1xTAvWk_gKd7Weka3UsK301w5Bnjtu_IIiNLx2-CSvRRsVMt8QQy3fY3qimr-_g_w-m3mW4nE4pY
         * code : 0
         * value : 05JkRFK6UtwzBZKy3RJfMYNAIWwrzhijKQH-kvPlpybABNrfF-81bdYmU1cAk0dY6TPxK_HUqjZs3jLvw9uOjvzopOgO3vcDdC1oDvQo1Hyip3-OAKoIicaJiUZx1DNx-6f8OK_vIrbixjKx0P2veOw5kYBtxP2svA7zDwMJSgX4-somcC00Xc3bB7XRNv01vAU74wdbrQ09SHVCzLJeBRoLMffyTDhVZsgAYWofHI7D4yeseffrcKo5dMHZ9qBCLuZcfo8yWDlc5cohEtSNOPWNQdADaoRT3Zh9q8w-j2AJPMaTfKfpo13slkAfHVKSeDr1moSb1uZw865_6bs5NWlxTHyO8yXO-cpLPHk6IOiglZ4gJpjtaUY30OUWw0w0Zq
         */

        private String csessionid;
        private int code;
        private String value;

        public String getCsessionid() {
            return csessionid;
        }

        public void setCsessionid(String csessionid) {
            this.csessionid = csessionid;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
