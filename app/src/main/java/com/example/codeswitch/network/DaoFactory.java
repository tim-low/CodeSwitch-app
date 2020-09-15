package com.example.codeswitch.network;

public class DaoFactory {
    public UserDao getUserDao() {
        return ApiManager.getInstance().create(UserDao.class);
    }
    public JobDao getJobDao () {
        return ApiManager.getInstance().create(JobDao.class);
    }
    public SavedJobDao getSavedJobDao() {
        return ApiManager.getInstance().create(SavedJobDao.class);
    }
    public SkillDao getSkillDao() {
        return ApiManager.getInstance().create(SkillDao.class);
    }
    public AuthResponseDao getAuthResponseDao() {
        return ApiManager.getInstance().create(AuthResponseDao.class);
    }
}
