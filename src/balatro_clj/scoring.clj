(ns balatro-clj.scoring
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
(defn conj-or-create-vec
  "If initial not-nil, will conj element to it. Otherwise, returns element in a new vector"
  [initial element]
  (if initial (conj initial element) [element]))

(defn create-rank-to-cards-map
  "Returns a map with key is rank, and value is a sequence of cards with that rank"
  [cards]
  (reduce (fn [accum card] (update accum (:rank card) conj-or-create-vec card)) {} cards))

(defn find-same-rank
  "Returns a sequence of n card sequences where cards are same rank. May return empty."
  [rank-count rank-to-cards-map]
  (->> rank-to-cards-map
       (vals)
       (filter #(= rank-count (count %)))))

;; TODO
(defn is-royal-flush?
  [cards]
  false)

;; TODO
(defn is-straight-flush?
  [cards]
  false)

;; TODO
(defn is-flush?
  [cards]
  false)

;; TODO
(defn is-straight?
  [cards]
  false)

(defn determine-poker-hand
  [cards]
  (let [rank-to-cards-map (create-rank-to-cards-map cards)
        four-of-a-kinds (find-same-rank 4 rank-to-cards-map)
        three-of-a-kinds (find-same-rank 3 rank-to-cards-map)
        pairs (find-same-rank 2 rank-to-cards-map)]
    (cond ;
      (is-royal-flush? cards) {:poker-hand :royal-flush
                               :cards cards}
      (is-straight-flush? cards) {:poker-hand :three-of-a-kind
                                  :cards (first four-of-a-kinds)}
      (seq four-of-a-kinds) {:poker-hand :four-of-a-kind
                             :cards (first four-of-a-kinds)}
      (is-flush? cards) {:poker-hand :flush
                         :cards cards}
      (is-straight? cards) {:poker-hand :straight
                            :cards  cards}
      (seq three-of-a-kinds) {:poker-hand :three-of-a-kind
                              :cards (first three-of-a-kinds)}
      (= 2 (count pairs)) {:poker-hand :two-pair
                           :cards (first pairs)}
      (= 1 (count pairs)) {:poker-hand :pair
                           :cards (first pairs)}
      :else {:poker-hand :high-card
             :cards [(cards/find-highest-value-card cards)]})))

(defn evaluate-card
  [initial-score card]
  (update initial-score :chips + (:chips card)))

; card 1: 5 chips (5 total)
; card 2: 10 chips (15 total)
(defn evaluate-scoring-sequence
  "Returns the steps used to evaluate the score."
  [jokers cards]
  (let [poker-hand (determine-poker-hand cards)]
    (map #(%) cards)))

; TODO: Scoring stuff needs to be a lot better!
(defn determine-score
  [jokers cards]
  (let [{poker-hand :poker-hand
         cards :cards} (determine-poker-hand cards)
        base-score (poker-hand poker-hand-to-base-scoring)]
    {:score (reduce #(evaluate-card %1 %2) base-score cards)
     :poker-hand poker-hand}))

(comment
  (determine-score [] {:poker-hand :high-card :cards [{:chips 1}]})
  (:high-card poker-hand-to-base-scoring))
