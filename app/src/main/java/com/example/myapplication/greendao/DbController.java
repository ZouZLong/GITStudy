package com.example.myapplication.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class DbController {
    /**
     * Helper
     */
    private DaoMaster.DevOpenHelper mHelper;//获取Helper对象   获取数据库
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * DaoMaster
     */
    //DaoMaster保存数据库对象（SQLiteDatabase）并管理特定模式的Dao类。
    // 它具有静态方法来创建表或将他们删除。
    // 其内部类OpenHelper和DevOpenHelper是在SQLite数据库中创建模式的SQLiteOpenHelper实现。
    private DaoMaster mDaoMaster;
    /**
     * DaoSession
     */
    //管理特定模式的所有可用Dao对象，您可以使用其中一个getter方法获取。
    // DaoSession还为实体提供了一些通用的持久性方法，如插入，加载，更新，刷新和删除。
    // 最后，DaoSession对象也跟踪一个身份范围。
    private DaoSession mDaoSession;
    /**
     * 上下文
     */
    private Context context;
    /**
     * dao
     */
    private InfoBeanDao infoBeanDao;

    private static DbController mDbController;

    /**
     * 获取单例
     */
    public static DbController getInstance(Context context) {
        if (mDbController == null) {
            synchronized (DbController.class) {
                if (mDbController == null) {
                    mDbController = new DbController(context);
                }
            }
        }
        return mDbController;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public DbController(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context, "person.db", null);
        mDaoMaster = new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        infoBeanDao = mDaoSession.getInfoBeanDao();
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(context, "person.db", null);
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     *
     * @return
     */
    private SQLiteDatabase getWritableDatabase() {
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(context, "person.db", null);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db;
    }

    /**
     * 会自动判定是插入还是替换
     *
     * @param infoBean
     */
    public void insertOrReplace(InfoBean infoBean) {
        infoBeanDao.insertOrReplace(infoBean);
    }

    /**
     * 插入一条记录，表里面要没有与之相同的记录
     *
     * @param infoBean
     */
    public long insert(InfoBean infoBean) {
        return infoBeanDao.insert(infoBean);
    }

    /**
     * 更新数据
     *
     * @param infoBean
     */
    public void update(InfoBean infoBean) {
        //拿到之前的记录
        InfoBean mOldPersonInfor = infoBeanDao.queryBuilder().where(InfoBeanDao.Properties.Id.eq(infoBean.getId())).build().unique();
        if (mOldPersonInfor != null) {
            mOldPersonInfor.setName("张三");
            infoBeanDao.update(mOldPersonInfor);
        }
    }

    /**
     * 按条件查询数据
     */
    public List<InfoBean> searchByWhere(String wherecluse) {
        List<InfoBean> personInfors = (List<InfoBean>)
                infoBeanDao.queryBuilder().where(InfoBeanDao.Properties.Name.eq(wherecluse)).build().unique();
        return personInfors;
    }

    /**
     * 查询所有数据
     */
    public List<InfoBean> searchAll() {
        List<InfoBean> personInfors = infoBeanDao.queryBuilder().list();
        return personInfors;
    }

    /**
     * 删除数据
     */
    public void delete(String wherecluse) {
        infoBeanDao.queryBuilder().where(InfoBeanDao.Properties.Name.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}