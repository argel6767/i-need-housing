import Link from "next/link"

export const Footer = () => {
    return (
        <footer className="footer bg-base-300 text-base-content p-10">

  <nav>
    <h6 className="footer-title">INeedHousing</h6>
    <Link href={"/about#about"} className="link link-hover">About us</Link>
    <Link href={"/about#contact"} className="link link-hover">Contact</Link>
    <Link href={"/about#contribute"} className="link link-hover">Contributing</Link>
  </nav>
</footer>
    )
}