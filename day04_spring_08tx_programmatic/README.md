# LearnSpringFramework-day3-使用Spring内置的事务控制接口-编程式事务控制

  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)


## Spring 编程式事务控制
- 配置事务模板对象:`<bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate"></bean>`
- ![IdtV35H](https://i.imgur.com/IdtV35H.png)
- 所有用到事务控制的地方都要加上:`transactionTemplate.execute(new TransactionCallback <Object>() {...}` 这段内容
- 会造成业务层重复代码又越来越多，**实际不常用**

## 实例
```java
public class AccountServiceImpl implements IAccountService {

    // 业务层需要调用持久层
    private IAccountDao accountDao;

    private TransactionTemplate transactionTemplate;

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void setAccountDao(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }


    @Override
    public Account findAccountById(Integer accountId) {
       return transactionTemplate.execute(new TransactionCallback<Account>() {
           @Override
           public Account doInTransaction(TransactionStatus transactionStatus) {
               return accountDao.findAccountById(accountId);
           }
       });
    }

    /**
     * 这里的转账操作只是示意，实际业务中肯定不能这样写
     * @param sourceName
     * @param targetName
     * @param money
     */
    @Override
    public void transfer(String sourceName, String targetName, float money) {
        transactionTemplate.execute(new TransactionCallback <Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                System.out.println("transfer开始...");
                // 2.1 根据名称查询转出账户余额
                Account source = accountDao.findAccountByName(sourceName);
                // 2.2 根据名称查询转入账户
                Account target = accountDao.findAccountByName(targetName);
                // 2.3 转出账户减钱
                source.setMoney(source.getMoney() - money);
                // 2.4 转入账户加钱
                target.setMoney(target.getMoney() + money);
                // 2.5 更新转出账户
                accountDao.updateAccount(source);
                int i = 1/0;
                // 2.6 更新转入账户
                accountDao.updateAccount(target);
                return null;
            }
        });
    }
}

```
