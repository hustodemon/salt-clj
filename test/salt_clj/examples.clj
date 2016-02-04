(ns salt-clj.examples
  (:require [salt-clj.core :refer :all]))

(def url "http://172.17.0.2:8000")

(def token
  (login! url {:username "root" 
               :password "root"
               :eauth "auto"}))
(logout! token)

(minions token)
(minions token :b0bba2673dda)

(minions! token {:tgt "*"
                 :fun "test.ping"})
(minions! token {:tgt "*"
                 :fun "cmd.run"
                 :arg "date"})
(jobs token :20160203164905066391)
(jobs token "20160203164905066391")
(jobs token :20160203075336876551)

(comment hook! token {:foo 1
              :bar "barbar"})
(comment events token)

(comment keys token)
