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

(defn logout! [token] ; todo check - it doesn't work
  (util/salt-post token "logout" nil))

(defn minions [token & mid]
  (util/salt-get token "minions" (util/null-safe-name (first mid)))); todo name?

(defn minions! [token data]
  (util/salt-post token "minions" data))
  
(defn jobs [token & jid]
  (util/salt-get token "jobs" (util/null-safe-name (first jid))))

(comment defn events [token] ; todo test
  (util/salt-get token "events"))

(comment defn keys [token] ; todo test
  (util/salt-get token "keys"))

(comment defn ws [token]
  (util/salt-get token "ws"))

(comment defn stats [token] ; probably doesn't work
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

