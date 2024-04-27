(ns balatro-clj.core  (:gen-class)
    (:require [balatro-clj.cards :as cards]))

; https://balatrogame.fandom.com/wiki/Poker_Hands
(def poker-hand-to-base-scoring
  {:high-card {:chips 5 :mult 1}
   :pair {:chips 10 :mult 2}
   :two-pair {:chips 20 :mult 2}
   :three-of-a-kind {:chips 30 :mult 3}
   :flush {:chips 35 :mult 4}
   :full-house {:chips 40 :mult 4}
   :four-of-a-kind {:chips 60 :mult 7}
   :straight-flush {:chips 100 :mult 8}
   :royal-flush {:chips 100 :mult 8} ;
   })

(defn sort-hand-by-rank
  [game-state]
  (update game-state :hand cards/sort-by-rank))

(defn evaluate-card
  [initial-score card]
  (update initial-score :chips + (:chips card)))

(defn determine-poker-hand
  [played-cards]
  (cond ; TODO: this
    :else {:poker-hand :high-card
           :cards [(cards/find-highest-value-card played-cards)]}))

; TODO: Scoring stuff needs to be a lot better!
(defn determine-score
  [played-poker-hand]
  (let [{poker-hand :poker-hand
         cards :cards} played-poker-hand
        base-score (poker-hand poker-hand-to-base-scoring)]
    (reduce #(evaluate-card %1 %2) base-score cards)))

(defn evaluate-played-hand
  [game-state]
  (let [poker-hand (determine-poker-hand (:played-cards game-state))
        score (determine-score poker-hand)]
    (update game-state :score #(+ % (* (:chips score) (:mult score))))))

(defn select-cards
  [game-state indexes]
  (-> game-state
      (assoc :played-cards (cards/keep-all-nth (:hand game-state) indexes))
      (update :hand #(cards/keep-rest-nth % indexes))))

(defn initial-game-state
  []
  {:hand []
   :played-cards []
   :deck (shuffle cards/initial-deck)
   :jokers []
   :score 0
   :hands 4
   :discards 3
   :money 8})

(defn start-round
  [game-state]
  (-> game-state
      (assoc :hand (subvec (:deck game-state) 0 10))
      (assoc :deck (subvec (:deck game-state) 10))))

(comment
  (determine-score {:poker-hand :high-card :cards [{:chips 1}]})
  (select-cards {:hand [1 2 3] :played-cards []} [0])
  (:high-card poker-hand-to-base-scoring))
