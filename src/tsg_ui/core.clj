(ns tsg-ui.core
    (:require [quil.core :as q]
            [quil.middleware :as m]
            [tsg-ui.ui :as ui])
    (:gen-class) )

(q/defsketch tsg-ui
  :title "TSG UI concepts"
  :size [500 500]
  ; setup function called only once, during sketch initialization.
  :setup ui/setup
  ; update-state is called on each iteration before draw-state.
  :update ui/update-state
  :draw ui/draw-state
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode]
  )

 (defn -main [& args]
     (q/sketch
       :title "TSG UI concepts"
       :size [500 500]
       :setup ui/setup
       :update ui/update-state
       :draw ui/draw-state
       :middleware [m/fun-mode]
       :features [:exit-on-close]
       )
)
