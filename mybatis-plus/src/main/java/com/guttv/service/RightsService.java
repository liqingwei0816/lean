package com.guttv.service;

import com.guttv.bean.Rights;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface RightsService {

    List<Rights> getList(Rights rights);

    Rights getOne(Rights rights);

    Integer delete(Integer id);

    Integer updateById(@NotNull Rights rights);
}
