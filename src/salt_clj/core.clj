(ns salt-clj.core
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.walk :as walk]))

; utils
(declare happy-get happy-post parse-happy-response)

(def url "http://172.17.0.2:8000/")

(defn login! [url params]
  (happy-post
    (str url "login")
    nil
    params))

(defn minions [token & mid]
  (happy-get
    (str url "minions/" (first mid))
    token
    nil))

(defn minions! [token data]
  (happy-post
    (str url "minions/")
    token
    data))

(defn jobs [token & jid]
  (happy-get
    (str url "jobs/" (first jid))
    token
    nil))

; utils
(defn token->secret [token]
  (:token (first (:return token))))

(defn parse-happy-response
  "Parses a body of a successful HTTP response."
  [res]
  (-> res
    :body
    json/read-str
    walk/keywordize-keys))

(defn happy-get
  "Performs GET on url with given security token and parameters, returns the
  body of the response."
  [url token params]; todo params form?
  (-> 
    (http/get url {:headers {:X-Auth-Token token}
                   :form-params params})
    parse-happy-response))

(defn happy-post
  "Performs POST on url with given security token and parameters, returns the
  body of the response."
  [url token params]
  (-> 
    (http/post url {:headers {:X-Auth-Token token}
                    :form-params params})
    parse-happy-response))

; examples
(comment
  (def token (token->secret
               (login! url {:username "root" 
                            :password "root"
                            :eauth "auto"})))
  
  (minions token)
  (minions! token {:tgt "*"
                   :fun "test.ping"})
  (happy-post  )

  (minions! token {:tgt "*"
                   :fun "cmd.run"
                   :arg "date"})

  
  (jobs token 20160203075336876551)
  (jobs token 20160203084658956495)

  (get-in
    (first
      (get-in
        (json/read-str
          (:body
            (http/get
              (str url "minions")
              {:headers {:X-Auth-Token (token-raw token)}})))
        ["return"]))
    ["b0bba2673dda" "kernel"])

  (http/post
    (str url "minions")
    {:form-params {:tgt "*"
                   :fun "test.ping"}
     :headers {:X-Auth-Token token}})

  (http/post
    (str url "minions")
    {:form-params {:tgt "*"
                   :fun "grains.items"}
     :headers {:X-Auth-Token (token-raw token)}})

  (json/read-str 
    (:body
      (http/get 
        (str url "jobs/20160202164546207189")
        {:headers {:X-Auth-Token (token-raw token)}}))))


; Things to do:
; 'login': Login,
; 'logout': Logout,
; 'minions': Minions,
; 'run': Run,
; 'jobs': Jobs,
; 'keys': Keys,
; 'events': Events,
; 'stats': Stats,

