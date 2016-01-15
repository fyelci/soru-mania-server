package com.fyelci.sorumania.service;

import com.fyelci.sorumania.domain.Lov;
import com.fyelci.sorumania.repository.LovRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by fatih on 14/1/16.
 */

@Service
@Transactional
public class LovService {


    @Inject
    private LovRepository lovRepository;

    @Transactional(readOnly = true)
    public Lov getLovById(Long id) {
        return lovRepository.findOne(id);
    }

}
