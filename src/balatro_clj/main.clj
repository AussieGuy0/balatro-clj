(ns balatro-clj.main
  (:require [balatro-clj.core :as core]
            [balatro-clj.cards :as cards]
            [clojure.string :as str]))

(def game-state
  (atom (core/initial-game-state)))

(defn swap-game-state!
  [f]
  (swap! game-state f))

(defn cards-to-str
  [hand]
  (map-indexed
   #(str
     %1
     ":"
     (cards/suit-to-emoji (:suit %2))
     (:rank %2)
     " ")
   hand))

(defn print-hand
  [cards]
  (println "Your current hand:" (cards-to-str cards)))

(defn print-played-cards
  [cards]
  (println "played cards:" (cards-to-str cards)))

(defn ask-for-input
  [message]
  (print message)
  (flush)
  (read-line))

(defn parse-card-selection
  [input]
  (map #(Integer/parseInt %) (str/split input #"\s")))

(defn run
  []
  (println "Welcome to balatro-clj!")
  (swap! game-state core/start-round)
  (while true
    (swap-game-state! core/sort-hand-by-rank)
    (print-hand (:hand @game-state))
    (swap-game-state! #(core/select-cards % (parse-card-selection (ask-for-input "Select cards: "))))
    (print-played-cards (:played-cards @game-state))
    (swap-game-state! core/evaluate-played-hand)
    (println "" (:last-score-details @game-state))
    (println "score: " (:score @game-state))
    (swap-game-state! core/next-round)
    ;
    ))

(defn -main
  [& args]
  (run))

(comment
  (run)
  (parse-card-selection "1 2 3"))

