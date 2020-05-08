import React from "react"
import { Link } from "gatsby"

import HomeLayout from "../components/homeLayout"
import SEO from "../components/seo"

const IndexPage = () => (
  <HomeLayout>
    <SEO title="Home" />
    <h1>Hi people</h1>
    <p>Welcome to your new Gatsby site.</p>
    <p>Now go build something great.</p>
  </HomeLayout>
)

export default IndexPage
