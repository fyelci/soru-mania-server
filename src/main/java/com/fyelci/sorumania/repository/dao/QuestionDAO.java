package com.fyelci.sorumania.repository.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by fatih on 5/1/16.
 */
@Repository
public class QuestionDAO {


    @Autowired
    private DataSource dataSource;


}
