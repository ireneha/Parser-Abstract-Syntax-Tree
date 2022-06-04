# R list functions

null <- function () return (c ())

cons <- function (head, tail) return (c (head, tail))

head <- function (list) return (list [1])

tail <- 
  function (list) {
    if (length (list) <= 1) {
      return (null ());
    }
    else {
      return (list [2 : length (list)])
    }
  }
