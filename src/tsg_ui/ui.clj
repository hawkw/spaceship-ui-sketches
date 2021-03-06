(ns tsg-ui.ui
    (:require [quil.core :as q]))

(def grid [
    [20 20 "NUC"] [55 20 "JET"] [90 20 "RCS"] [125 20 "JMP"]
    [20 65 "PLC"] [55 65 "NAV"] [90 65 "COM"] [125 65 "BIO"]
    ]
)

(def green  [0 255 0] )
(def yellow [255 255 0] )
(def red    [255 0 0] )

(defn setup []
    ; Set frame rate to 30 frames per second.
    (q/frame-rate 30)
    (q/color-mode :rgb)
    ; setup function returns initial state. It contains
    ; circle color and position.
    (q/text-mode :shape)
    (q/text-size 10)
    {:labelfont (q/create-font "Monaco" 10 false)
     :colors [ [green 1] [green 1] [green 1] [green 1]
               [green 1] [green 1] [green 1] [green 1] ]
     :fault-img (q/load-image "fault.png")
     :fault-flag false
    })

(defn gencolor [newcolor [color age]]
    (if (and
            (== 0 (mod age 15))  ; enough framps have passed since last color swap
            (= 3 (rand-int 4)) ) ; random chance to swap
        (if (= green color) [newcolor 1] [green 1])
        [color (+ 1 age)])
    )

(defn update-state [state]
    (-> state
        (assoc :fault-flag
            (or
                (:fault-flag state)
                (and
                    (< 300 (q/frame-count)) ; run normally for about 10sec before faulting
                    (= 50 (rand-int 51))    ; random chance to fault
                )
            )
        )
        (assoc :colors
            (doall
                (map (partial gencolor (if (:fault-flag state) red yellow) )
                     (:colors state)
                )
            )
        )

    )
)

(defn bl-dist [n] (+ 5 (/ 2 n)))

(defn blinkenlight [ [x y] [r g b] age]
    (q/fill r g b)
    (q/no-stroke)
    (q/rect x y 12 12)
    (q/no-fill)
    (q/stroke 255)
    (q/rect x y 14 14)
    )

(defn draw-state [state]
    ; Clear the sketch by filling it with black
    (q/background 0)
    ;(q/no-stroke)
    (q/rect-mode :radius)
    (q/text-align :center :center)
    (q/fill 255)
    ;(print state "\n")
    (doseq [
        [ [x y text][[r g b] age] ]
        (map vector grid (:colors state))
            ]
        (blinkenlight [x y] [r g b] age)
    )
    (q/fill 255)
    (doseq [[x y text] grid]
        (q/text-font (:labelfont state))
        (q/text text x (+ y 20))
    )
    (if (:fault-flag state)
        (q/image (:fault-img state) 145 5)
        ()
    )
)
