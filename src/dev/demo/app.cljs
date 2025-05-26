(ns demo.app
  (:require
    [shadow.graft.modules :as gm]
    [shadow.graft :as graft]
    [cljs.reader :as reader]))

(defmethod graft/scion "log" [_ _]
  (graft/reloadable
    (js/console.warn "html told me to log")))

(defn fetch-fragment [container]
  (-> (js/fetch "/html-fragment.html" {:headers {"Content-Type" "text/html"}})
      (.then (fn [value] (.text value)))
      (.then (fn [value] (-> container .-innerHTML  (set! value))))))

(defmethod graft/scion "toggle-section" [opts container]
  ;; there are a variety of ways to get specific DOM elements
  ;; I prefer data attribute over just class names or ids
  (let [opener (.querySelector container "[data-opener]")
        body (.querySelector container "[data-body]")]

    (.addEventListener opener "click"
      (fn [e]
        (fetch-fragment body)
        (-> body .-classList (.toggle "hidden"))))))

(defn init []
  (gm/init reader/read-string))
