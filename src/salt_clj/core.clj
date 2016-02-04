(ns salt-clj.core
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.walk :as walk]
            [salt-clj.util :as util]))

(defn login! [url params]
  (let [salt-token (-> 
                     (http/post (str url "/login") {:form-params params})
                     (util/parse-happy-response)
                     (util/token->secret))]
    {:url url
     :password salt-token}))

; todo check - it doesn't work
(defn logout! [token]
  (util/salt-post token "logout" nil))

(defn minions 
  ([token]
   (minions token nil))
  ([token mid]
   (util/salt-get token "minions" (util/nil-safe-name mid))))

(defn minions! [token data]
  (util/salt-post token "minions" data))
  
(defn jobs
  ([token] 
   (jobs token nil))
  ([token jid]
   (util/salt-get token "jobs" (util/nil-safe-name jid))))

; todo test (404 in my setup also in the java lib)
(comment defn hook! [token data]
  (util/salt-post token "hook" data))

; todo test - for me it freezes, java lib says "handshake error"
(comment defn events [token] 
  (util/salt-get token "events"))

; todo test (doesn't work in the java lib either - 404)
(comment defn keys [token]
  (util/salt-get token "keys"))

; todo test - doesn't even exist in the java lib
(comment defn ws [token]
  (util/salt-get token "ws"))

; 404 in java lib too
(comment defn stats [token]
  (util/salt-get token "stats"))

; examples
(comment
  (def url "http://172.17.0.2:8000")
  (def token
    (login! url {:username "root" 
                 :password "root"
                 :eauth "auto"}))

  (logout! token)

  (minions token)

  (minions! token {:tgt "*"
                   :fun "test.ping"})
  (minions! token {:tgt "*"
                   :fun "cmd.run"
                   :arg "date"})
  
  (jobs token :20160203164905066391)
  (jobs token :20160203075336876551)
)

