(ns tsg-ui.ui
    (:require [quil.core :as q]))

(def grid [
    [20 20 "NUC"] [55 20 "JET"] [90 20 "RCS"] [125 20 "JMP"]
    [20 55 "PLC"] [55 55 "NAV"] [90 55 "COM"] [125 55 "BIO"]
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
    (q/text-size 10)
    {:labelfont (q/create-font "HelveticaNeue-CondensedBlack" 10 true)
     :colors [ [green 1] [green 1] [green 1] [green 1]
               [green 1] [green 1] [green 1] [green 1] ]
     :fault-img (q/load-image "fault.png")
     :fault-flag false
    })

(defn gencolor [newcolor [color age]]
    (if (and
            (== 0 (mod age 15)) ; enough framps have passed since last color swap
            (= 3 (rand-int 4))  ; random chance to swap
            )
        (if (= green color) [newcolor 1] [green 1])
        [color (+ 1 age)])
    )

(defn update-state [state]
    (-> state
        (assoc :colors
            (doall
                (map (partial gencolor (if (:fault-flag state) red yellow) )
                     (:colors state)
                )
            )
        )
        (assoc :fault-flag
            (or
                (:fault-flag state)
                (and
                    (< 300 (q/frame-count)) ; run normally for about 10sec before faulting
                    (= 50 (rand-int 51))    ; random chance to fault
                )
            )
        )
    )
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
        ;(print rgb)
        (q/fill r g b)
        (q/rect x y 15 15)
    )
    (q/fill 0)
    (doseq [[x y text] grid]
        (q/text-font (:labelfont state))
        (q/text text x y)
    )
    (if (:fault-flag state)
        (q/image (:fault-img state) 145 5)
        ()
    )
)
