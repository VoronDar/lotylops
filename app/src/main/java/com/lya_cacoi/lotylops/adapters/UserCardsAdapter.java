package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.NewCardFragment;
import com.lya_cacoi.lotylops.activities.utilities.CardChangable;
import com.lya_cacoi.lotylops.adapters.units.newCardElementUnit;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.cards.VocabularyCardNet;
import com.lya_cacoi.lotylops.cards.VocabularyCardNetHolder;

import java.util.ArrayList;
import java.util.List;

import static com.lya_cacoi.lotylops.activities.NewCardFragment.generateId;

public class UserCardsAdapter extends RecyclerView.Adapter<UserCardsAdapter.ButtonLetterViewHolder> implements CardChangable {

        private final List<VocabularyCardNet> units;
        private BlockListener acceptListener;
        private BlockListener declineListener;
        private final Context context;
        private int openPage;
        private ArrayList<newCardElementUnit> elements;
        private NewCardElementAdapter adapter;

        public VocabularyCardNet getUnit(int position){
            return units.get(position);
        }

        public UserCardsAdapter(Context context, List<VocabularyCardNet> blocks) {
            this.context = context;
            this.openPage = -1;
            this.units = blocks;
        }

        public interface BlockListener {
            void onClick(int position);

        }

        public List<VocabularyCardNet> getUnits() {
            return units;
        }

        public void setAcceptListener(BlockListener block_listener) {
            this.acceptListener = block_listener;
        }
        public void setDeclineListener(BlockListener block_listener) {
            this.declineListener = block_listener;
        }

        @NonNull
        @Override
        public ButtonLetterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.change_user_card, viewGroup, false);
            return new ButtonLetterViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ButtonLetterViewHolder holder, final int position) {
            VocabularyCardNet unit = units.get(position);
            if (unit.getPart() != null && unit.getPart().length() != 0){
                unit.setPart("существительное");
            }

            holder.setIsRecyclable(false);

            holder.word.setText(unit.getWord());
            holder.translate.setText(unit.getTranslate());


            if (openPage == position){
                holder.moreInfo.setVisibility(View.VISIBLE);
                holder.mainInfo.setVisibility(View.GONE);

                elements = new ArrayList<>();
                elements.add(new newCardElementUnit(context.getString(R.string.word), unit.getWord(), true, NewCardFragment.ElIds.word));
                elements.add(new newCardElementUnit(context.getString(R.string.translate), unit.getTranslate(), true, NewCardFragment.ElIds.transl));
                elements.add(new newCardElementUnit(context.getString(R.string.meaningLearn), unit.getMeaning(), true, NewCardFragment.ElIds.meaning));
                elements.add(new newCardElementUnit(context.getString(R.string.meaningNative), unit.getMeaningNative(), true, NewCardFragment.ElIds.meaningNative));
                elements.add(new newCardElementUnit(context.getString(R.string.part), unit.getPart(), true, NewCardFragment.ElIds.part));
                elements.add(new newCardElementUnit(context.getString(R.string.group), unit.getGroup(), false, NewCardFragment.ElIds.group));
                elements.add(new newCardElementUnit(context.getString(R.string.synonym), unit.getSynonym(), false, NewCardFragment.ElIds.synonym));
                elements.add(new newCardElementUnit(context.getString(R.string.antonym), unit.getAntonym(), false, NewCardFragment.ElIds.antonym));
                elements.add(new newCardElementUnit(context.getString(R.string.help), unit.getHelp(), false, NewCardFragment.ElIds.help));
                elements.add(new newCardElementUnit(context.getString(R.string.transcription), unit.getTranscription(), false, NewCardFragment.ElIds.transcript));
                elements.add(new newCardElementUnit(context.getString(R.string.exampleLearn), unit.getExample(), true, NewCardFragment.ElIds.example));
                elements.add(new newCardElementUnit(context.getString(R.string.exampleNative), unit.getExampleTranslate(), true, NewCardFragment.ElIds.exampleNative));
                elements.add(new newCardElementUnit(context.getString(R.string.exampleTrainLearn), unit.getTrain(), true, NewCardFragment.ElIds.train));
                elements.add(new newCardElementUnit(context.getString(R.string.exampleTrainNative), unit.getTrainTranslate(), true, NewCardFragment.ElIds.trainNative));
                adapter = new NewCardElementAdapter(true, context, elements, this);
                holder.moreInfo.setAdapter(adapter);
                holder.moreInfo.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                adapter.notifyDataSetChanged();
            }
            else{
                holder.moreInfo.setVisibility(View.GONE);
                holder.mainInfo.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(v -> {
                if (openPage != position) {
                    if (openPage != -1){
                    VocabularyCard card = new VocabularyCardNetHolder(units.get(openPage));
                    for (int i = 0; i < elements.size(); i++) {
                        setToCard(card, i);
                    }
                    Log.i("main", "changed card " + card);
                        elements.clear();
                }
                    openPage = position;
                }
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return units.size();
        }

        class ButtonLetterViewHolder extends RecyclerView.ViewHolder {

            private final  RecyclerView moreInfo;
            private final  View mainInfo;
            private final  TextView word;
            private final  TextView translate;

            public ButtonLetterViewHolder(@NonNull View itemView) {
                super(itemView);
                Button accept = itemView.findViewById(R.id.buttonAccept);
                Button decline = itemView.findViewById(R.id.buttonCancel);
                View buttons = itemView.findViewById(R.id.buttons);
                moreInfo = itemView.findViewById(R.id.more_info);
                mainInfo = itemView.findViewById(R.id.main_info);
                word = itemView.findViewById(R.id.word);
                translate = itemView.findViewById(R.id.translate);

                //itemView.findViewById(R.id.send).setVisibility(View.GONE);

                accept.setOnClickListener(v -> {
                    if (acceptListener != null) {
                        if (elements != null && elements.size() > 0) {
                            VocabularyCard card = new VocabularyCardNetHolder(units.get(openPage));
                            for (int i = 0; i < elements.size(); i++) {
                                setToCard(card, i);
                            }
                        }
                        acceptListener.onClick(getAdapterPosition());
                    }
                });
                decline.setOnClickListener(v -> {
                    if (declineListener != null)
                        declineListener.onClick(getAdapterPosition());
                });
            }
        }

    @Override
    public void setToCard(VocabularyCard card, int position) {
            if (elements == null || elements.size() == 0) return;

        newCardElementUnit n = elements.get(position);
        if (n.getId() == NewCardFragment.ElIds.word) {
            card.setWord(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.train && getValue(n) != null) {
            //if (card.getTrain() == null) {
            //    card.setTrain(n.getValue());
            //}
            //else
            if (card.getTrains() == null || card.getTrains().size() == 0) {
                card.setTrains(new ArrayList<>(1));
                card.getTrains().add(new VocabularyCard.Sentence(getValue(n), null, true, generateId(), card.getId()));
            } else {
                card.getTrains().get(0).setSentence(getValue(n));
            }
        }
        //else if (n.getId() == ElIds.train && isWriteLearn)
        //    continue;
        else if (n.getId() == NewCardFragment.ElIds.trainNative && getValue(n) != null) {
            if (card.getTrains() == null || card.getTrains().size() == 0) {
                card.setTrains(new ArrayList<>(1));
                card.getTrains().add(new VocabularyCard.Sentence(null, getValue(n), true, generateId(), card.getId()));
            } else {
                card.getTrains().get(0).setTranslate(getValue(n));
            }
        } else if (n.getId() == NewCardFragment.ElIds.transl) {
            card.setTranslate(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.meaning) {
            card.setMeaning(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.meaningNative) {
            card.setMeaningNative(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.example) {
            if (card.getExamples() == null || card.getExamples().size() == 0) {
                card.setExamples(new ArrayList<>(1));
                card.getExamples().add(new VocabularyCard.Sentence(getValue(n), null, true, generateId(), card.getId()));
            } else
                card.getExamples().get(0).setSentence(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.exampleNative) {
            if (card.getExamples() == null || card.getExamples().size() == 0) {
                card.setExamples(new ArrayList<>(1));
                card.getExamples().add(new VocabularyCard.Sentence(null, getValue(n), true, generateId(), card.getId()));
            } else
                card.getExamples().get(0).setTranslate(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.mem) {
        } else if (n.getId() == NewCardFragment.ElIds.part) {
            card.setPart(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.group) {
            card.setGroup(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.transcript) {
            card.setTranscription(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.help) {
            card.setHelp(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.synonym) {
            card.setSynonym(getValue(n));
        } else if (n.getId() == NewCardFragment.ElIds.antonym) {
            card.setAntonym(getValue(n));
        }
        elements.get(position).setValue(getValue(n));
        try {
            adapter.notifyDataSetChanged();
        } catch (IllegalStateException ignored){
        }
    }

    private String getValue(newCardElementUnit n){
            return ((n.getValue() == null || n.getValue().length() < 2)?n.getPrevValue():n.getValue());
    }

    @Override
    public VocabularyCard getCard() {
        return (VocabularyCard)( new VocabularyCardNetHolder(units.get(openPage)));
    }
}
