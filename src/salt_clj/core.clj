(ns salt-clj.core
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.walk :as walk]
            [salt-clj.util :as util]))

(defn login!
  "Log in. Example params:
    {:username \"root\" 
     :password \"root\"
     :eauth \"auto\"}"
  [url params]
  (let [salt-token (-> 
                     (http/post (str url "/login") {:form-params params})
                     (util/parse-happy-response)
                     (util/token->secret))]
    {:url url
     :password salt-token}))

; todo check - it doesn't work (although the API returns 200!!!!!)
(defn logout!
  "Log out."
  [token]
  (util/salt-post token "logout" nil))

(defn minions 
  "List minion(s). Optional param: minion id"
  ([token]
   (minions token nil))
  ([token mid]
   (util/salt-get token "minions" (util/nil-safe-name mid))))

(defn minions!
  "Run a job based on given data. Example data (running command):
     {:username \"root\"
      :password \"root\"
      :eauth \"auto\"
      :fun \"cmd.run\"
      :arg \"sleep 3 && date\"
      :tgt \"*\"
      :client \"local\"}\"}
  Example data (pinging minions):  
     {:username \"root\"
      :password \"root\"
      :eauth \"auto\"
      :fun \"test.ping\"
      :tgt \"*\"
      :client \"local\"}\"."
  [token data]
  (util/salt-post token "minions" data))
  
(defn jobs
  "List job(s)."
  ([token] 
   (jobs token nil))
  ([token jid]
   (util/salt-get token "jobs" (util/nil-safe-name jid))))

(defn run!
  "Synchronously run a job under given credentials (doesn't require the login
  token) based on given data. See doc for minions! for examples."
  [token data]
  (util/salt-post token "run" data))

; todo test (404 in my setup also in the java lib)
(comment defn hook! [token data]
  (util/salt-post token "hook" data))

; todo test - for me it freezes, java lib says "handshake error"
(comment defn events [token] 
  (util/salt-get token "events"))

; todo test (doesn't work in the java lib either - 404)
(comment defn keys [token]
  (util/salt-get token "keys"))

; todo implement
(comment defn keys! [token])

; todo test - doesn't even exist in the java lib
(comment defn ws [token]
  (util/salt-get token "ws"))

; 404 in java lib too
(comment defn stats [token]
  (util/salt-get token "stats"))

