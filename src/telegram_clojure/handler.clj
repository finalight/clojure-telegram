(ns telegram-clojure.handler
  (:require [compojure.core :refer :all]
            [clojure.java.io :as io]
            [clojure.java.io :refer :all]
            [clojure.string :as str]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [clj-http.client :as client]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params wrap-json-body]]
            [ring.util.response :refer [response]]
            ))

(defn read-env-file
  "read environment variable definitions from file into a map."
  ([]
     (try
       (with-open [rdr (io/reader ".env")]
         (->> (line-seq rdr)
              (map #(str/replace % #"(^export\s+)|([#].*)" ""))
              (map str/trim)
              (remove str/blank?)
              (map #(str/replace % #"[']" "\""))
              (map #(str/split % #"(\s*=\s*)|(:\s+)"))
              (map (fn [[k v]]                    
                     (case v
                       "\"\"" [k  ""]
                       nil    [k nil]
                       [k (str/replace v #"[\"]" "")])))
              (into {})))
       (catch java.lang.Throwable e
         (.printStackTrace e)
         (throw (Error. (format "Could not load configuration file: %s" (.getCanonicalPath file))))))))

; (prn ((env :telegram-api-key))
; (def token (get (read-env-file) "TELEGRAM_API_KEY"))
(def token (System/getenv "TELEGRAM_API_KEY"))
(def bot-url (System/getenv "BOT_URL"))
(def api-url (str "https://api.telegram.org/bot" token "/"))

(defn set-webhook []
  ; (println "token is " token)
  (client/get (str api-url "setWebhook?url=" bot-url))
  (println "assume it's connected"))

(defn get-webhook []
  (client/get (str api-url "getWebhookInfo")))  

(defn send-message [chat-id text]
  (client/post (str api-url "sendMessage") {:form-params {:chat_id chat-id, :text text} :content-type :json})
  )

(wrap-json-response
  (defroutes app-routes
    (GET "/" [] "testing")
    ; (POST "/" request )))
    (POST "/" {request :body}  
      ; (send-message)
      ; (prn (slurp (get-in request [:body])))
      ; (print "id is " (get-in request [:message :chat :id]))
      (prn "id is " (get-in request ["message" "chat" "id"]))
      (prn "text is " (get-in request ["message" "text"]))
      (send-message (get-in request ["message" "chat" "id"]) (get-in request ["message" "text"]))
      ; (prn "message is " (slurp (get-in request [:body :chat :text])))
      )
    (GET "/test" [] "test here")
    (GET "/webhook" [] (set-webhook))
    (GET "/getwebhook" [] (get-webhook))
    (GET "/token" [] (str "token is " token))
    (route/not-found "Not Found")))


(def init (set-webhook))
(def app
  (wrap-json-body app-routes api-defaults)
  )
