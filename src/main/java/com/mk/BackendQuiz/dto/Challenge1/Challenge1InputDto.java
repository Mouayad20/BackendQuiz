package com.mk.BackendQuiz.dto.Challenge1;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Challenge1InputDto {
    private int[] array;
    private int expectedSum;
}
