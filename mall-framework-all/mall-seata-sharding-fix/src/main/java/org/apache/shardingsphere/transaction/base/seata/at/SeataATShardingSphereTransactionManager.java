
package org.apache.shardingsphere.transaction.base.seata.at;

import io.seata.config.FileConfiguration;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.core.rpc.netty.RmNettyRemotingClient;
import io.seata.core.rpc.netty.TmNettyRemotingClient;
import io.seata.rm.RMClient;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.tm.TMClient;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.shardingsphere.infra.database.core.type.DatabaseType;
import org.apache.shardingsphere.infra.exception.core.ShardingSpherePreconditions;
import org.apache.shardingsphere.transaction.api.TransactionType;
import org.apache.shardingsphere.transaction.base.seata.at.exception.SeataATApplicationIDNotFoundException;
import org.apache.shardingsphere.transaction.base.seata.at.exception.SeataATDisabledException;
import org.apache.shardingsphere.transaction.exception.TransactionTimeoutException;
import org.apache.shardingsphere.transaction.spi.ShardingSphereTransactionManager;

public final class SeataATShardingSphereTransactionManager implements ShardingSphereTransactionManager {
    private final Map<String, DataSource> dataSourceMap = new HashMap();
    private final String applicationId;
    private final String transactionServiceGroup;
    private final boolean enableSeataAT;
    private final int globalTXTimeout;

    public SeataATShardingSphereTransactionManager() {
        FileConfiguration config = new FileConfiguration("seata.conf");
        this.enableSeataAT = config.getBoolean("shardingsphere.transaction.seata.at.enable", true);
        this.applicationId = config.getConfig("client.application.id");
        this.transactionServiceGroup = config.getConfig("client.transaction.service.group", "default");
        this.globalTXTimeout = config.getInt("shardingsphere.transaction.seata.tx.timeout", 60);
    }

    public void init(Map<String, DatabaseType> databaseTypes, Map<String, DataSource> dataSources, String providerType) {
        if (this.enableSeataAT) {
            this.initSeataRPCClient();
            dataSources.forEach((key, value) -> {
                this.dataSourceMap.put(key, new DataSourceProxy(value));
            });
        }

    }

    private void initSeataRPCClient() {
        ShardingSpherePreconditions.checkNotNull(this.applicationId, SeataATApplicationIDNotFoundException::new);
        TMClient.init(this.applicationId, this.transactionServiceGroup);
        RMClient.init(this.applicationId, this.transactionServiceGroup);
    }

    public TransactionType getTransactionType() {
        return TransactionType.BASE;
    }

    public boolean isInTransaction() {
        this.checkSeataATEnabled();
//        return null != RootContext.getXID();
        return null != SeataTransactionHolder.get();
    }

    public Connection getConnection(String databaseName, String dataSourceName) throws SQLException {
        this.checkSeataATEnabled();
        return ((DataSource)this.dataSourceMap.get(databaseName + "." + dataSourceName)).getConnection();
    }

    public void begin() {
        this.begin(this.globalTXTimeout);
    }

    public void begin(int timeout) {
        try {
            ShardingSpherePreconditions.checkState(timeout >= 0, TransactionTimeoutException::new);
            this.checkSeataATEnabled();
            GlobalTransaction globalTransaction = GlobalTransactionContext.getCurrentOrCreate();
            globalTransaction.begin(timeout * 1000);
            SeataTransactionHolder.set(globalTransaction);
        } catch (TransactionException var3) {
            TransactionException $ex = var3;
            try {
                throw $ex;
            } catch (TransactionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void commit(boolean rollbackOnly) {
        try {
            this.checkSeataATEnabled();

            try {
                SeataTransactionHolder.get().commit();
            } finally {
                SeataTransactionHolder.clear();
                RootContext.unbind();
                SeataXIDContext.remove();
            }

        } catch (TransactionException var6) {
            TransactionException $ex = var6;
            try {
                throw $ex;
            } catch (TransactionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void rollback() {
        try {
            this.checkSeataATEnabled();

            try {
                SeataTransactionHolder.get().rollback();
            } finally {
                SeataTransactionHolder.clear();
                RootContext.unbind();
                SeataXIDContext.remove();
            }

        } catch (TransactionException var5) {
            TransactionException $ex = var5;
            try {
                throw $ex;
            } catch (TransactionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void checkSeataATEnabled() {
        ShardingSpherePreconditions.checkState(this.enableSeataAT, SeataATDisabledException::new);
    }

    public void close() {
        this.dataSourceMap.clear();
        SeataTransactionHolder.clear();
        RmNettyRemotingClient.getInstance().destroy();
        TmNettyRemotingClient.getInstance().destroy();
    }

    public String getType() {
        return TransactionType.BASE.name();
    }
}
