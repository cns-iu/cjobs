
# Granger Causality

**Statsmodels** is a python library for statistical and econometric analysis (see - http://www.statsmodels.org/stable/index.html)

Source: Seabold S, Perktold J. Statsmodels: Econometric and Statistical Modeling with Python. 
PROC 9th PYTHON Sci CONF [Internet]. 2010 [cited 2018 May 5]; 
Available from: https://conference.scipy.org/proceedings/scipy2010/pdfs/seabold.pdf

> the Granger causality test adds a term causal term to the linear prediction model and compares that to a similar linear model without that term. This comparison of Residual Sum of Squares is done by an F-test, which takes a value near 1 if there is no causality, and deviates from 1 if there is a difference in RSS. This value is then translated to a p-value using the F-distribution. The 'strength' of the causality is expressed in the F-value. 
If it is close to 1, it's weak, if it is high, the causality has a big effect.

Parameters in the test:

1. ssr based F test
2. ssr based chi2 test
3. likelihood ratio test
4. parameter F test = ssr based F test

The comparison of Residual Sum of Squares is done by an F-test, which takes a value near 1 if there is no causality, 
and deviates from 1 if there is a difference in RSS. This value is then translated to a p-value using the F-distribution. 
The **strength** of the causality is expressed in the F-value. 
If it is close to 1, it's weak, if it is high, the causality has a big effect.

# Kullback-Leibler Divergence

- KL is used to measure two distributions. It will calculate how much information is lost when we compare one distribution with another.

More information: https://www.countbayesie.com/blog/2017/5/9/kullback-leibler-divergence-explained


