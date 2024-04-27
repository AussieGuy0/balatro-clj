(ns balatro-clj.cards)

(defn- get-chip-value
  [rank]
  (cond
    (< rank 10) rank
    (< rank 14) 11
    :else 12))

(def rank-to-face-card
  {11 :jack
   12 :queen
   13 :king
   14 :ace ; Ace is not technically a face card but shut up.
   })

(defn- create-card
  "rank 2-10 will be numeral cards. 11-14 will be face cards + ace"
  [suit value]
  {:suit suit
   :rank (cond
           (< value 2) (throw (Exception. "Rank can not be lower than 2"))
           (<= value 10) value
           (<= value 14) (get rank-to-face-card value)
           :else (throw (Exception. "Rank can not be higher than 14")))
   :chips (get-chip-value value)
   :value value})

(defn sort-by-rank
  [cards]
  (sort-by :value > cards))

(defn find-highest-value-card
  [cards]
  (apply max-key :value cards))

(defn keep-all-nth
  [cards indexes]
  (map #(nth cards %) indexes))

(defn keep-rest-nth
  [cards indexes]
  (keep-indexed #(if (some #{%1} indexes)
                   nil
                   %2)
                cards))
(def suits
  [:clubs :diamonds :hearts :spades])

(def suit-to-emoji
  {:diamonds "♦️"
   :hearts "♥️"
   :spades "♠️"
   :clubs "♣️"})

(def initial-deck
  "Generates a standard 52 card deck"
  (for [rank (range 2 15)
        suit suits]
    (create-card suit rank)))

(comment
  (keep-all-nth [1 2 3] [0 2]) ; 1 3
  (keep-rest-nth [1 2 3] [0]) ; 2 3
  )
