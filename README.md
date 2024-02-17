# Reading material: 
**1. OAuth 2.0 Standard Protocol and its components**: https://auth0.com/intro-to-iam/what-is-oauth-2 <br>
**2. JJWTs Library Support/Documentations:** https://github.com/jwtk/jjwt?tab=readme-ov-file#jwt-example <br>

Difference between JWT, JWS and JWE: <br>
JWT - JSON Web token (Without signature), JWS - Signed JSON Web tokens (Signed by some secretkey and all of the key-holder can read it) 
> JWS just ensures that the payload is not changed by anyone, by signing it with key. It doesn't mean that someone cannot read it.
{Header}.{Payload}.{Signature}
Signature is something that can be checked by Auth server and the microservices that have the secret key to generate the singature. All of them can know, if the payload or even header part
is tampered or not.
Signature = function (header,payload,secretkey)

and JWE - Encrypted JSON Web tokens (fully encrypted - not readable by any other service)
