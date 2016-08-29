package com.lilun.passionlife.cloudplatform.bean;

/**
 * Created by Administrator on 2016/8/29.
 */
public class RxbusEvent {
    public static class reflashModules{}
    public static class reflashOasAndModules{}
    public static class addModule{
        private OrganizationService module;

        public addModule(OrganizationService module) {
            this.module = module;
        }

        public OrganizationService getModule() {
            return module;
        }
    }
    public static class editdModule{
        private OrganizationService module;

        public editdModule(OrganizationService module) {
            this.module = module;
        }

        public OrganizationService getModule() {
            return module;
        }
    }
    public static class deleteModule{
        public String serviceId;

        public deleteModule(String serviceId) {
            this.serviceId = serviceId;
        }
    }
}
