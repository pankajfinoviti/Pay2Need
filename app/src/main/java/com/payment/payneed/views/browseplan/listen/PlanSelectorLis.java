package com.payment.payneed.views.browseplan.listen;

import com.payment.payneed.views.browseplan.model.DataItem;

public interface PlanSelectorLis {
        void onButtonSelect(int p, DataItem model);

        void onImgExpand(int p, DataItem model);
    }