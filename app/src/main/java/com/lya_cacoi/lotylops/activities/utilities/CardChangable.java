package com.lya_cacoi.lotylops.activities.utilities;

import com.lya_cacoi.lotylops.cards.VocabularyCard;

public interface CardChangable {
    void setToCard(VocabularyCard card, int position);
    VocabularyCard getCard();
}
