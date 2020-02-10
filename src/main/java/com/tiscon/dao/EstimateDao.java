package com.tiscon.dao;

import com.tiscon.domain.*;
import com.tiscon.dto.UserOrderDto;
import com.tiscon.service.LocalSearch;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * 引越し見積もり機能においてDBとのやり取りを行うクラス。
 *
 * @author Oikawa Yumi
 */
@Component
public class EstimateDao {

    /** データベース・アクセスAPIである「JDBC」を使い、名前付きパラメータを用いてSQLを実行するクラス */
    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    /**
     * コンストラクタ
     *
     * @param parameterJdbcTemplate NamedParameterJdbcTemplateクラス
     */
    public EstimateDao(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    /**
     * 顧客テーブルに登録する。
     *
     * @param customer 顧客情報
     * @return 登録件数
     */
    public int insertCustomer(Customer customer) {
        String sql = "INSERT INTO CUSTOMER(OLD_PREFECTURE_ID, NEW_PREFECTURE_ID, CUSTOMER_NAME, TEL, EMAIL, OLD_ADDRESS, NEW_ADDRESS)"
                + " VALUES(:oldPrefectureId, :newPrefectureId, :customerName, :tel, :email, :oldAddress, :newAddress)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int resultNum = parameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(customer), keyHolder);
        customer.setCustomerId(keyHolder.getKey().intValue());
        return resultNum;
    }

    /**
     * オプションサービス_顧客テーブルに登録する。
     *
     * @param optionService オプションサービス_顧客に登録する内容
     * @return 登録件数
     */
    public int insertCustomersOptionService(CustomerOptionService optionService) {
        String sql = "INSERT INTO CUSTOMER_OPTION_SERVICE(CUSTOMER_ID, SERVICE_ID)"
                + " VALUES(:customerId, :serviceId)";
        return parameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(optionService));
    }

    /**
     * 顧客_荷物テーブルに登録する。
     *
     * @param packages 登録する荷物
     * @return 登録件数
     */
    public int[] batchInsertCustomerPackage(List<CustomerPackage> packages) {
        String sql = "INSERT INTO CUSTOMER_PACKAGE(CUSTOMER_ID, PACKAGE_ID, PACKAGE_NUMBER)"
                + " VALUES(:customerId, :packageId, :packageNumber)";
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(packages.toArray());

        return parameterJdbcTemplate.batchUpdate(sql, batch);
    }

    /**
     * 都道府県テーブルに登録されているすべての都道府県を取得する。
     *
     * @return すべての都道府県
     */
    public List<Prefecture> getAllPrefectures() {
        String sql = "SELECT PREFECTURE_ID, PREFECTURE_NAME FROM PREFECTURE";
        return parameterJdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Prefecture.class));
    }

    /**
     * 都道府県間の距離を取得する。
     *
     * @param dto ユーザーデータ
     * @return 距離[km]
     */
    public double getDistance(UserOrderDto dto) {
        Properties propertyFrom = new Properties();
        try {
            String appid = "dj00aiZpPVNNWXcwSkdjWndmTiZzPWNvbnN1bWVyc2VjcmV0Jng9YjM-";
            String query = dto.getOldPrefectureId() + dto.getOldAddress();
            List<Properties> pois = new LocalSearch(appid).search(query);
            if (pois.size() == 0) {
                throw new Error();
            }
            propertyFrom = pois.get(0);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }
        Properties propertyTo = new Properties();
        try {
            String appid = "dj00aiZpPVNNWXcwSkdjWndmTiZzPWNvbnN1bWVyc2VjcmV0Jng9YjM-";
            String query = dto.getNewPrefectureId() + dto.getNewAddress();
            List<Properties> pois = new LocalSearch(appid).search(query);
            if (pois.size() == 0) {
                throw new Error();
            }
            propertyTo = pois.get(0);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }
        com.mkyong.http.Java11HttpClientExample obj = new com.mkyong.http.Java11HttpClientExample();
        try {
            return obj.getDistance(propertyFrom, propertyTo);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
            return 0;
        }
    }

    /**
     * 荷物ごとの段ボール数を取得する。
     *
     * @param packageId 荷物ID
     * @return 段ボール数
     */
    public int getBoxPerPackage(int packageId) {
        String sql = "SELECT BOX FROM PACKAGE_BOX WHERE PACKAGE_ID = :packageId";

        SqlParameterSource paramSource = new MapSqlParameterSource("packageId", packageId);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
    }

    /**
     * 段ボール数に応じたトラック料金を取得する。
     *
     * @param boxNum 総段ボール数
     * @return 料金[円]
     */
    public int getPricePerTruck(int boxNum) {
        int num4t;
        int num2t = 0;
        int res;
        /*String sql = "SELECT PRICE FROM TRUCK_CAPACITY WHERE MAX_BOX >= :boxNum ORDER BY PRICE LIMIT 1";

        SqlParameterSource paramSource = new MapSqlParameterSource("boxNum", boxNum);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
        */
        num4t = boxNum / 200;
        res = boxNum - (num4t * 200);
        if ((res > 0) && (res <= 80)) {
            num2t += 1;
        } else {
            if ((res > 80) && (res < 200)) {
                num4t += 1;
            }
        }
        return (50000 * num4t + 30000 * num2t);
    }

    /**
     * オプションサービスの料金を取得する。
     *
     * @param serviceId サービスID
     * @return 料金
     */
    public int getPricePerOptionalService(int serviceId) {
        String sql = "SELECT PRICE FROM OPTIONAL_SERVICE WHERE SERVICE_ID = :serviceId";

        SqlParameterSource paramSource = new MapSqlParameterSource("serviceId", serviceId);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
    }
}
