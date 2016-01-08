package com.fyelci.sorumania.service;

import com.fyelci.sorumania.domain.*;
import com.fyelci.sorumania.repository.LovRepository;
import com.fyelci.sorumania.repository.ScoreHistoryRepository;
import com.fyelci.sorumania.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;

/**
 * Created by fatih on 7/1/16.
 */
@Service
@Transactional
public class ScoreService {

    @Inject
    private ScoreHistoryRepository scoreHistoryRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private LovRepository lovRepository;

    public void addScoreToUser(User user, Long contentId, Long transactionTypeId, Long scoreTypeId) {
        Lov scoreType = lovRepository.findOne(scoreTypeId);

        //Yaptigi islemi skor gecmisine ekle.
        ScoreHistory scoreHist = new ScoreHistory();
        scoreHist.setUser(user);
        scoreHist.setCreateDate(ZonedDateTime.now());
        scoreHist.setContentId(contentId);
        scoreHist.setScore(scoreType.getIntParam1());
        scoreHist.setTransactionType(lovRepository.findOne(transactionTypeId));
        scoreHistoryRepository.save(scoreHist);

        //Kullaniciya yaptigi islemden dolayi puan ver.
        if (user.getTotalScore() == null) {
            user.setTotalScore(scoreType.getIntParam1().longValue());
        } else {
            user.setTotalScore(user.getTotalScore().longValue() + scoreType.getIntParam1().longValue());
        }
        userRepository.save(user);
    }

}
