(ns salt-clj.examples
  (:require [salt-clj.core :refer :all]))

; URL of our API server
(def url "http://172.17.0.2:8000")

; Let's login and remember our access token (we will need it for all subsequent
; calls (except 'run')).
(def token
  (login! url {:username "root" 
               :password "root"
               :eauth "auto"}))

; We can take a look at all our minions
(minions token)

; ...or at a particular minion. You can use either a keyword form:
(minions token :b0bba2673dda)
; ... or a string to identify the minion
(minions token "b0bba2673dda")

; Let's fire up some commands!
; First example is just a ping function:
(minions! token {:tgt "*"
                 :fun "test.ping"})
; Funnier case - running a command (in this case, we'll remember the id of the
; job to fetch the results later)
(def jid
  (:jid
    (minions! token {:tgt "*"
                     :fun "cmd.run"
                     :arg "date"})))

; Let's take a look at the job results:
(jobs token jid)

; We can identify the job by a keyword, too:
(jobs token (keyword jid)) ; something like :20160203164905066391

; run! is handy when you want to run one-shot task without logging before and
; wait for the result (run! behaves in a synchronous manner)
(run! token {:username "root"
             :password "root"
             :eauth "auto"
             :fun "cmd.run"
             :arg "sleep 3 && date"
             :tgt "*"
             :client "local"})

(logout! token)

