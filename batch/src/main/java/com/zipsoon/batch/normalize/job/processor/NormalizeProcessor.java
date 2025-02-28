package com.zipsoon.batch.normalize.job.processor;

import com.zipsoon.batch.score.model.ScoreType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NormalizeProcessor implements ItemProcessor<ScoreType, ScoreType> {
    @Override
    public ScoreType process(ScoreType scoreType) {
        log.info("Processing normalization for score type: {}", scoreType.getName());
        return scoreType;
    }
}