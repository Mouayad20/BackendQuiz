package com.mk.BackendQuiz.dto.Challenge1;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Challenge1OutputDto {
    List<List<Integer>> indexes;
}
