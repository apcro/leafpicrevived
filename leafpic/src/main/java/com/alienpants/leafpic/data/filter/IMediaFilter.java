package com.alienpants.leafpic.data.filter;

import com.alienpants.leafpic.data.Media;

/**
 * Created by dnld on 4/10/17.
 */

public interface IMediaFilter {
    boolean accept(Media media);
}
