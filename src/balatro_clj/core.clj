(ns balatro-clj.core  (:gen-class)
    (:require [balatro-clj.cards :as cards]
              [balatro-clj.scoring :as scoring]))

(defn sort-hand-by-rank
  [game-state]
  (update game-state :hand cards/sort-by-rank))

(defn evaluate-played-hand
  [game-state]
  (let [score-details (scoring/determine-score (:jokers game-state) (:played-cards game-state))
        score (:score score-details)]
    (-> game-state
        (assoc :last-score-details score-details)
        (update :score #(+ % (* (:chips score) (:mult score)))))))

(defn select-cards
  [game-state indexes]
  (-> game-state
      (assoc :played-cards (cards/keep-all-nth (:hand game-state) indexes))
      (update :hand #(cards/keep-rest-nth % indexes))))

(defn draw-n-cards
  [game-state n]
  (let [deck (:deck game-state)]
    (-> game-state
        (update :hand into (take n deck))
        (assoc :deck (subvec deck n)))))

(defn start-round
  [game-state]
  (draw-n-cards game-state 10))

(defn next-round
  [game-state]
  (let [hand-count (count (:hand game-state))]
    (-> game-state
        (update :hands dec)
        (assoc :played-cards [])
        (draw-n-cards (- 10 hand-count)))))

(defn initial-game-state
  []
  {:hand []
   :played-cards []
   :deck (shuffle cards/initial-deck)
   :jokers []
   :last-score-details {}
   :score 0
   :hands 4
   :discards 3
   :money 8})

(comment
  (draw-n-cards {:hand [] :deck [1 2 3]} 1) ; hand [1] deck [2 3]
  (select-cards {:hand [1 2 3] :played-cards []} [0]))
