package com.example.demosqltest.spy6;

import com.p6spy.engine.common.PreparedStatementInformation;
import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.event.JdbcEventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomSingleEventListener extends JdbcEventListener {
    private static List<String> sqlList = new ArrayList<>();

    public Optional<String> getSqlEvent() {
        Optional<String> first = sqlList.stream().findFirst();
        if (first.isPresent()) {
            sqlList.remove(0);
        }
        return first;
    }

    public void cleanUpSqlList(){
        sqlList.clear();
    }
    public boolean isEventPresent() {
        return !sqlList.isEmpty();
    }

    public CustomSingleEventListener() {
        super();
    }



    public void onBeforeExecuteQuery(PreparedStatementInformation statementInformation) {
        String sqlWithValues = statementInformation.getSqlWithValues();
        sqlList.add(sqlWithValues);
        super.onBeforeExecute(statementInformation);
    }

    @Override
    public void onBeforeExecuteBatch(StatementInformation statementInformation) {
        String sqlWithValues = statementInformation.getSqlWithValues();
        sqlList.add(sqlWithValues);
        super.onBeforeExecuteBatch(statementInformation);
    }

    @Override
    public void onBeforeExecuteUpdate(PreparedStatementInformation statementInformation) {
        String sqlWithValues = statementInformation.getSqlWithValues();
        sqlList.add(sqlWithValues);
        super.onBeforeExecuteUpdate(statementInformation);
    }
}
